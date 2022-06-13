package com.zakiis.file.portal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zakiis.file.portal.domain.constants.CommonConstants;
import com.zakiis.file.portal.exception.ErrorEnum;
import com.zakiis.file.portal.model.Bucket;
import com.zakiis.file.portal.model.inner.Access;
import com.zakiis.file.portal.service.tool.ValidationTool;
import com.zakiis.security.exception.NoPermissionException;

@Service
public class BucketService {

	@Autowired
	ApplicationCacheService cacheService;

	public void checkChannelCanRead(String ak, String bucketName) {
		Bucket bucket = cacheService.getBucketMap().get(bucketName);
		ValidationTool.notNull(bucket, ErrorEnum.BUCKET_NOT_EXISTS);
		if (CommonConstants.ACESS_MODE_PUBLIC.equals(bucket.getAccessMode())) {
			return;
		}
		Access access = Optional.ofNullable(bucket.getAccess())
			.map(map -> map.get(ak))
			.orElse(null);
		if (access == null || !access.isCanRead()) {
			throw new NoPermissionException(String.format("%s has no read privilege on %s", ak, bucketName));
		}
	}
	
	public void checkChannelCanWrite(String ak, String bucketName) {
		Bucket bucket = cacheService.getBucketMap().get(bucketName);
		ValidationTool.notNull(bucket, ErrorEnum.BUCKET_NOT_EXISTS);
		Access access = Optional.ofNullable(bucket.getAccess())
			.map(map -> map.get(ak))
			.orElse(null);
		if (access == null || !access.isCanWrite()) {
			throw new NoPermissionException(String.format("%s has no write privilege on %s", ak, bucketName));
		}
	}
}
