package com.dzjt.cbs.uam.security.admin.vo;

import java.util.List;
import java.util.Map;

public class EasyUiTree {
	private Long id;  
    private String text;  
    private Boolean checked = false;  
    private List<Map<String, Object>> children;  
    private String state = "closed";
    
    private String iconCls="icon-group-bank";
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	
	public List<Map<String, Object>> getChildren() {
		return children;
	}
	public void setChildren(List<Map<String, Object>> children) {
		this.children = children;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}  
    
    
}
