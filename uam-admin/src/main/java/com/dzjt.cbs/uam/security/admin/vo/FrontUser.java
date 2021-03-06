package com.dzjt.cbs.uam.security.admin.vo;

import com.dzjt.cbs.uam.security.admin.entity.Group;
import com.dzjt.cbs.uam.security.admin.entity.UserCompany;
import com.dzjt.cbs.uam.security.api.vo.authority.PermissionInfo;

import java.util.List;

/**
 * Created by XT on 2018/2/22.
 */
public class FrontUser {
    public String id;
    public String username;
    public String name;
    private Integer baseOrgId;
    private String description;
    private String image;
    private List<PermissionInfo> menus;
    private List<PermissionInfo> elements;
    private List<Group> groups;
    private List<UserCompany> companyList;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(Integer baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public List<PermissionInfo> getMenus() {
        return menus;
    }

    public void setMenus(List<PermissionInfo> menus) {
        this.menus = menus;
    }

    public List<PermissionInfo> getElements() {
        return elements;
    }

    public void setElements(List<PermissionInfo> elements) {
        this.elements = elements;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<UserCompany> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<UserCompany> companyList) {
        this.companyList = companyList;
    }
}
