package com.zakiis.file.core.domain.dto;

import java.io.Serializable;

import com.zakiis.file.core.domain.constants.CommonConstants;
import com.zakiis.file.core.exception.ServiceError;
import com.zakiis.file.core.exception.ServiceException;

public class ResponseDTO<T> implements Serializable {

	private static final long serialVersionUID = -8796561214486544190L;
	
	private String code;
	private String message;
	T data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	public static ResponseDTO<Object> buildErrorResponse(ServiceException e) {
		ResponseDTO<Object> dto = new ResponseDTO<Object>();
		dto.setCode(e.getCode());
		dto.setMessage(e.getMessage());
		return dto;
	}
	
	public static ResponseDTO<Object> buildErrorResponse(ServiceError e) {
		ResponseDTO<Object> dto = new ResponseDTO<Object>();
		dto.setCode(e.getCode());
		dto.setMessage(e.getMessage());
		return dto;
	}
	
	public static ResponseDTO<Object> ok() {
		ResponseDTO<Object> dto = new ResponseDTO<Object>();
		dto.setCode(CommonConstants.SUCCESS_CODE);
		dto.setMessage(CommonConstants.SUCCESS_MESSAGE);
		return dto;
	}

}
