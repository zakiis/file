package com.zakiis.file.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class LogFilter implements WebFilter {

	Logger log = LoggerFactory.getLogger(LogFilter.class);
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
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
