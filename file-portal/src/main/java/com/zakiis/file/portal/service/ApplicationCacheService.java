package com.zakiis.file.portal.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.zakiis.file.portal.domain.constants.CommonConstants;
import com.zakiis.file.portal.model.Bucket;
import com.zakiis.file.portal.model.Channel;

@Service
public class ApplicationCacheService {
	
	@Autowired
	ReactiveMongoTemplate mongoTemplate;
	/** key is ak */
	Map<String, Channel> channelMap = new HashMap<String, Channel>();
	/** key is bucket */
	Map<String, Bucket> bucketMap = new HashMap<String, Bucket>();

	public void init() {
		refreshChannelMap();
		refreshBucketMap();
	}
	
	public void refreshBucketMap() {
		mongoTemplate.query(Bucket.class)
			.all()
			.subscribe(bucket -> {
				getBucketMap().put(bucket.getName(), bucket);
			});
	}

	public void refreshChannelMap() {
		mongoTemplate.query(Channel.class)
		.matching(Criteria.where("status").is(CommonConstants.STATUS_ACTIVE))
		.all()
		.subscribe(channel -> {
			getChannelMap().put(channel.getAk(), channel);
		});
	}

	public Map<String, Channel> getChannelMap() {
		return channelMap;
	}

	public Map<String, Bucket> getBucketMap() {
		return bucketMap;
	}
}
