package com.zakiis.file.core.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.zakiis.file.core.boot.autoconfigure.properties.FileCoreProperties;
import com.zakiis.file.core.domain.constants.FileConstants;
import com.zakiis.file.core.exception.ErrorEnum;
import com.zakiis.file.core.exception.ServiceException;
import com.zakiis.file.core.model.FileEntity;
import com.zakiis.file.core.service.tool.FileTool;
import com.zakiis.file.core.service.tool.ValidationTool;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FileService {
	
	@Autowired
	FileCoreProperties fileCoreProperties;
	@Autowired
	ReactiveMongoTemplate mongoTemplate;

	public Mono<Void> saveFile(Flux<DataBuffer> body, String bucket, String fileKey) throws IOException {
		ValidationTool.notEmpty(bucket, "bucket");
		ValidationTool.notEmpty(fileKey, "file key");
		Query query = Query.query(Criteria.where("_id").is(fileKey));
		Mono<FileEntity> fileEntityMono = mongoTemplate.findOne(query, FileEntity.class, bucket);
		return fileEntityMono.hasElement()
			.flatMap(exists -> {
				if (exists) {
					return replaceFile(fileEntityMono, query, bucket, body);
				} else {
					return insertFile(bucket, fileKey, body);
				}
			}).flatMap(fileEntity -> updateFileSize(fileKey, bucket, fileEntity.getFilePath()));
	}
	
	private Mono<FileEntity> insertFile(String bucket, String fileKey, Flux<DataBuffer> body) {
		String directory = fileCoreProperties.getBasePath() + "/" + bucket + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		String filePathStr = directory + "/" + fileKey;
		FileEntity fileEntity = FileTool.buildFileEntity(fileKey, filePathStr);
		Mono<FileEntity> insertFileEntityMono = mongoTemplate.insert(fileEntity, bucket);
		Path filePath = FileTool.createFile(directory, fileKey);
		return insertFileEntityMono.then(DataBufferUtils.write(body, filePath))
				.then(Mono.just(fileEntity));
	}
	
	private Mono<FileEntity> replaceFile(Mono<FileEntity> fileEntityMono, Query query, String bucket, Flux<DataBuffer> body) {
		return fileEntityMono.flatMap(fileEntity -> {
			if (FileConstants.FILE_STATUS_ARCHIVE.equals(fileEntity.getStatus())) {
				throw new ServiceException(ErrorEnum.ARCHIVE_FILE_CANT_CHANGED);
			}
			Update update = Update.update("updateTime", new Date());
			Path filePath = Paths.get(fileEntity.getFilePath());
			return mongoTemplate.updateFirst(query, update, FileEntity.class, bucket)
				.then(DataBufferUtils.write(body, filePath))
				.then(Mono.just(fileEntity));
		});
	}
	
	private Mono<Void> updateFileSize(String fileKey, String bucket, String filePathStr) {
		long fileSize = new File(filePathStr).length();
		Update update = Update.update("fileSize", fileSize);
		Query query = Query.query(Criteria.where("_id").is(fileKey));
		return mongoTemplate.updateFirst(query, update, FileEntity.class, bucket)
				.then();
	}
	
	public Flux<DataBuffer> downloadFile(String bucket, String fileKey, DataBufferFactory bufferFactory) {
		Query query = Query.query(Criteria.where("_id").is(fileKey));
		return mongoTemplate.findOne(query, FileEntity.class, bucket)
			.switchIfEmpty(Mono.error(new ServiceException(ErrorEnum.FILE_NOT_EXISTS)))
			.flatMapMany(fileEntity -> {
				Path filePath = Paths.get(fileEntity.getFilePath());
				return DataBufferUtils.read(filePath, bufferFactory, 1024 * 100, StandardOpenOption.READ);
			});
	}
}
