package com.zakiis.file.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.zakiis.file.core.boot.autoconfigure.properties.FileCoreProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileCoreProperties.class)
public class FileCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileCoreApplication.class, args);
	}

}
