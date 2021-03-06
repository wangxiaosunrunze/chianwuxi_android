package com.wuxi.domain;

import java.io.Serializable;

/**
 * 
 * @author 杨宸  智佳  依申请公开  部门
 */

public class ApplyDept implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String doProjectId;
	private String depId;
	private String depName;
	private boolean isNull;
	private String zhinanUrl;
	
	public String getDoProjectId() {
		return doProjectId;
	}
	public void setDoProjectId(String doProjectId) {
		this.doProjectId = doProjectId;
	}
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	public String getZhinanUrl() {
		return zhinanUrl;
	}
	public void setZhinanUrl(String zhinanUrl) {
		this.zhinanUrl = zhinanUrl;
	}
}
