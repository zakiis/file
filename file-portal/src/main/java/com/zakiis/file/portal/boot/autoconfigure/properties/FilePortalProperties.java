package com.zakiis.file.portal.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.portal")
public class FilePortalProperties {

	/** file core url */
	private String fileCoreUrl;

	public String getFileCoreUrl() {
		return fileCoreUrl;
	}

	public void setFileCoreUrl(String fileCoreUrl) {
		this.fileCoreUrl = fileCoreUrl;
	}

	
}
