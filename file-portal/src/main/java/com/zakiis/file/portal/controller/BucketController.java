package com.zakiis.file.portal.controller;

import java.io.IOException;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zakiis.file.portal.domain.constants.RolesConstants;
import com.zakiis.file.portal.domain.dto.ResponseDTO;
import com.zakiis.security.annotation.Permission;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/file-portal/file")
public class BucketController {

	@Permission(roles = {RolesConstants.BUCKET_WRITE})
	@PostMapping("/{bucket}")
	public Mono<ResponseDTO<Object>> uploadFile(@PathVariable String bucket, @PathVariable String fileKey, ServerHttpRequest request) throws IOException {
		return Mono.just(ResponseDTO.ok());
	}
}
