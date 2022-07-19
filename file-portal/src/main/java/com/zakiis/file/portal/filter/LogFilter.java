package com.zakiis.file.portal.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter implements WebFilter {

	Logger log = LoggerFactory.getLogger(LogFilter.class);
	
	Set<String> excludePath = new HashSet<String>(Arrays.asList("/health"));
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String path = exchange.getRequest().getPath().value();
		if (excludePath.contains(path)) {
			return chain.filter(exchange);
		}
		log.info("{} {} start", exchange.getRequest().getMethodValue(), exchange.getRequest().getURI());
		long start = System.currentTimeMillis();
		return chain.filter(exchange)
				.then(Mono.fromRunnable(() -> {
					long end = System.currentTimeMillis();
					log.info("{} {} end, time elapse {} ms", exchange.getRequest().getMethodValue(), exchange.getRequest().getURI()
							, end - start);
				}));
	}

}
