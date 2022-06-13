package com.zakiis.file.portal.model;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;

import com.zakiis.file.portal.model.inner.Access;

public class Bucket {

	@Id
	String name;
	String description;
	private String accessMode;
	private Date createTime;
	private Date updateTime;
	/** key is ak */
	private Map<String, Access> access;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Map<String, Access> getAccess() {
		return access;
	}
	public void setAccess(Map<String, Access> access) {
		this.access = access;
	}
	public String getAccessMode() {
		return accessMode;
	}
	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}
	
}
