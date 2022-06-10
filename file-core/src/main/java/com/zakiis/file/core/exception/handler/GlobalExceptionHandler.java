package com.zakiis.file.core.exception.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zakiis.file.core.domain.constants.CommonConstants;
import com.zakiis.file.core.domain.dto.ResponseDTO;
import com.zakiis.file.core.exception.ServiceError;
import com.zakiis.file.core.exception.ServiceException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ResponseDTO<Object>> serviceException(ServiceException e) {
		log.warn("{}: code:{}, message:{}\n\tat {}", e.getClass().getSimpleName(), e.getCode()
				, e.getMessage(), getStackTrace(e, 3));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentType(MediaType.APPLICATION_JSON)
					.body(ResponseDTO.buildErrorResponse(e));
	}
	
	@ExceptionHandler(ServiceError.class)
	public ResponseEntity<ResponseDTO<Object>> serviceError(ServiceError e) {
		log.error("got an service error", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentType(MediaType.APPLICATION_JSON)
					.body(ResponseDTO.buildErrorResponse(e));
	}
	
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ResponseDTO<Object>> error(Throwable e) {
		log.error("got an uncaught exception", e);
		ResponseDTO<Object> dto = new ResponseDTO<Object>();
		dto.setCode(CommonConstants.ERROR_CODE);
		String message = Optional.ofNullable(e.getMessage()).orElse(CommonConstants.ERROR_MESSAGE);
		dto.setMessage(message);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentType(MediaType.APPLICATION_JSON)
					.body(dto);
	}

	private Object getStackTrace(Throwable e, int lineCount) {
		if (e != null && e.getStackTrace() != null && e.getStackTrace().length > 0) {
			List<String> stackTraces = new ArrayList<String>();
			for (int i = 0; i < e.getStackTrace().length && i < lineCount; i++) {
				stackTraces.add(e.getStackTrace()[i].toString());
			}
			return StringUtils.join(stackTraces, "\n\t");
		}
		return null;
	}
}
