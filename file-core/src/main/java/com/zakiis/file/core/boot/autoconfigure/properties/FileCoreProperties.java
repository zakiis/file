package com.zakiis.file.core.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.core")
public class FileCoreProperties {

	/** base path of file saved */
	private String basePath;

	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}
