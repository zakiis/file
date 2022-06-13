package com.zakiis.file.portal.exception;

public enum ErrorEnum implements IError {
	
	FIELD_REQUIRE_NOT_EMPTY("100001", "%s can't be empty"),
	FILE_NOT_EXISTS("100002", "file not exists"),
	WRITE_FILE_FAILED("100003", "write file failed"),
	READ_FILE_FAILED("100004", "read file failed"),
	ARCHIVE_FILE_CANT_CHANGED("100005", "archive file can't be changed"),
	
	CHANNEL_NOT_EXISTS("200001", "channel not exists"),
	BUCKET_NOT_EXISTS("200002", "bucket not exists"),
	UNAUTHORIZED("401", "UNAUTHORIZED"),
	UNKNOWN_ERROR("999999", "System busy, please try it later"),
	;
	String code;
	String message;
	
	private ErrorEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
