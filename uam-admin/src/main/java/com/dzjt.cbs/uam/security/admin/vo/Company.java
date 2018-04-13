package com.dzjt.cbs.uam.security.admin.vo;

public class Company {
	private Long id;
	private String companyId;
	private String companyName;
	private Long parentId;
	private String vGuid; // 父公司的唯一ID 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getvGuid() {
		return vGuid;
	}
	public void setvGuid(String vGuid) {
		this.vGuid = vGuid;
	}
	
	
}
