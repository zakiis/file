package com.zakiis.file.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.zakiis.file.portal.boot.autoconfigure.properties.FilePortalProperties;
import com.zakiis.file.portal.domain.constants.CommonConstants;
import com.zakiis.file.portal.domain.dto.ResponseDTO;
import com.zakiis.file.portal.exception.ServiceException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FileService {
	
	final WebClient webClient = WebClient.create();
	@Autowired
	FilePortalProperties filePortalProperties;

	public Mono<ResponseDTO<Object>> uploadFile(String bucketName, String fileKey, Flux<DataBuffer> body) {
		String uploadURL = filePortalProperties.getFileCoreUrl() + String.format(CommonConstants.uploadFileURL, bucketName, fileKey);
		return webClient.post()
			.uri(uploadURL)
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(BodyInserters.fromDataBuffers(body))
			.retrieve()
			.onStatus(HttpStatus::isError, response -> {
				return response.bodyToMono(new ParameterizedTypeReference<ResponseDTO<Object>>() {})
					.map(dto -> new ServiceException(dto.getCode(), dto.getMessage()));
			})
			.bodyToMono(new ParameterizedTypeReference<ResponseDTO<Object>>() {});
	}
	
	public Flux<DataBuffer> downloadFile(String bucketName, String fileKey) {
		String downloadURL = filePortalProperties.getFileCoreUrl() + String.format(CommonConstants.uploadFileURL, bucketName, fileKey);
		return webClient.get()
				.uri(downloadURL)
				.retrieve()
				.bodyToFlux(DataBuffer.class);
	}
}
