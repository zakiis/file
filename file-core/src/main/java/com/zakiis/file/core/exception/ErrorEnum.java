package com.zakiis.file.core.exception;

public enum ErrorEnum implements IError {
	
	FIELD_REQUIRE_NOT_EMPTY("100001", "%s can't be empty"),
	FILE_NOT_EXISTS("100002", "file not exists"),
	WRITE_FILE_FAILED("100003", "write file failed"),
	READ_FILE_FAILED("100004", "read file failed"),
	ARCHIVE_FILE_CANT_CHANGED("100005", "archive file can't be changed"),
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
