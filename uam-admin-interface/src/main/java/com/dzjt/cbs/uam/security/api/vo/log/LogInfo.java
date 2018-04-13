package com.dzjt.cbs.uam.security.api.vo.log;

import java.io.Serializable;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 * @date 2017-07-01 11:18
 */
public class LogInfo implements Serializable{

    private Integer companyId;

    private Integer menuId;
    private Integer optId;

    private String menu;
    private String opt;

    private String uri;
    private String method;

    private Date crtTime;
    private String crtUser;
    private String crtName;
    private String crtHost;
    private String companyName;

    public LogInfo(Integer companyId,Integer menuId,Integer optId,String menu, String option,
                   String uri, String method, Date crtTime, String crtUser, String crtName, String crtHost) {
        this.companyId = companyId;
        this.menuId = menuId;
        this.optId = optId;
        this.menu = menu;
        this.opt = option;
        this.uri = uri;
        this.method = method;
        this.crtTime = crtTime;
        this.crtUser = crtUser;
        this.crtName = crtName;
        this.crtHost = crtHost;
    }

    public LogInfo() {
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String option) {
        this.opt = option;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    public String getCrtName() {
        return crtName;
    }

    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    public String getCrtHost() {
        return crtHost;
    }

    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
