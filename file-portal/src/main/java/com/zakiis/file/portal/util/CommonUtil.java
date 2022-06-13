package com.zakiis.file.portal.util;

import java.util.Optional;

import org.springframework.http.server.reactive.ServerHttpRequest;

import com.zakiis.file.portal.domain.constants.CommonConstants;

public interface CommonUtil {

	static String getAk(ServerHttpRequest request) {
		return Optional.ofNullable(request.getHeaders().getFirst(CommonConstants.X_ACCESS_KEY))
				.orElse(request.getQueryParams().getFirst(CommonConstants.AK));
	}
	
	static String getSign(ServerHttpRequest request) {
		return Optional.ofNullable(request.getHeaders().getFirst(CommonConstants.X_SIGN))
				.orElse(request.getQueryParams().getFirst(CommonConstants.SIGN));
	}
}
