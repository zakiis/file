package com.zakiis.file.portal.filter;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.zakiis.common.JsonUtil;
import com.zakiis.file.portal.domain.dto.ResponseDTO;
import com.zakiis.file.portal.exception.ErrorEnum;
import com.zakiis.file.portal.exception.ServiceError;
import com.zakiis.file.portal.exception.ServiceException;
import com.zakiis.file.portal.exception.handler.GlobalExceptionHandler;
import com.zakiis.file.portal.model.Channel;
import com.zakiis.file.portal.service.ApplicationCacheService;
import com.zakiis.file.portal.service.tool.ValidationTool;
import com.zakiis.file.portal.util.CommonUtil;
import com.zakiis.file.portal.util.SignUtil;
import com.zakiis.security.PermissionUtil;
import com.zakiis.security.annotation.Permission;
import com.zakiis.security.exception.NoPermissionException;

import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class AuthorizationFilter implements WebFilter {
	
	@Autowired
	RequestMappingHandlerMapping handlerMapping;
	@Autowired
	ApplicationCacheService cacheService;
	@Autowired
	GlobalExceptionHandler globalExceptionHandler;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		//TODO exclude some public path
		ServerHttpRequest request = exchange.getRequest();
		Mono<HandlerMethod> handlerMethodMono = handlerMapping.getHandler(exchange).cast(HandlerMethod.class);
		return handlerMethodMono.zipWhen(handlerMethod -> {
			try {
				if (handlerMethod.hasMethodAnnotation(Permission.class)) {
					Permission permission = handlerMethod.getMethodAnnotation(Permission.class);
					String ak = CommonUtil.getAk(request);
					String sign = CommonUtil.getSign(request);
					ValidationTool.notEmpty(ak, "Request header X-ACCESS-KEY or query parameter ak");
					ValidationTool.notEmpty(sign, "Request header X-SIGN or query parameter sign");
					Channel channel = Optional.ofNullable(cacheService.getChannelMap())
							.map(m -> m.get(ak))
							.orElse(null);
					ValidationTool.notNull(channel, ErrorEnum.CHANNEL_NOT_EXISTS);
					SignUtil.validateSign(ak, channel.getSk(), request.getMethodValue(), request.getURI().getPath(), sign);
					PermissionUtil.checkFunctionAccess(channel.getRoles(), permission);
				}
				return chain.filter(exchange);
			} catch (Throwable e) {
				return processException(e, exchange);
			}
		}).map(tuple -> tuple.getT2());
	}
	
	Mono<Void> processException(Throwable e, ServerWebExchange exchange) {
		ResponseEntity<ResponseDTO<Object>> responseEntity = null;
		if (e instanceof ServiceException) {
			responseEntity = globalExceptionHandler.serviceException((ServiceException)e);
		} else if (e instanceof ServiceError) {
			responseEntity = globalExceptionHandler.serviceError((ServiceError)e);
		} else if (e instanceof NoPermissionException) {
			responseEntity = globalExceptionHandler.noPermissionException((NoPermissionException)e);
		} else {
			responseEntity = globalExceptionHandler.throwable(e); 
		}
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(responseEntity.getStatusCode());
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		DataBuffer bodyDataBuffer = response.bufferFactory().wrap(JsonUtil.toJson(responseEntity.getBody()).getBytes(StandardCharsets.UTF_8));
		return response.writeWith(Mono.just(bodyDataBuffer));
	}
}
