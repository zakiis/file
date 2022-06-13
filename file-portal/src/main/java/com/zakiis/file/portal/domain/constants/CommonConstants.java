package com.zakiis.file.portal.domain.constants;

public interface CommonConstants {

	String SUCCESS_CODE = "000000";
	String SUCCESS_MESSAGE = "success";
	
	String X_ACCESS_KEY = "X-ACCESS-KEY";
	String AK = "ak";
	String X_SIGN = "X-SIGN";
	String SIGN = "sign";
	
	Integer STATUS_INACTIVE = 0;
	Integer STATUS_ACTIVE = 1;
	
	String ACESS_MODE_PUBLIC = "public";
	String ACESS_MODE_PRIVATE = "private";
	
	/** params: 1-bucket 2-file key*/
	String uploadFileURL = "/v1/file-core/file/%s/%s";
	/** params: 1-bucket 2-file key*/
	String downloadFileURL = "/v1/file-core/file/%s/%s";
}
