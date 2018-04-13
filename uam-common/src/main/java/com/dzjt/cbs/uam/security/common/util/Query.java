package com.dzjt.cbs.uam.security.common.util;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//当前页码
    private int page = 1;
    //每页条数
    private int rows = 10;

    private String orderBy;

    public Query(Map<String, Object> params,String orderBy){
        this.initPage(params);
        this.orderBy = orderBy;
    }
    public Query(Map<String, Object> params){
        this.initPage(params);
    }

    private void initPage(Map<String, Object> params){
        params.remove("_");
        this.putAll(params);
        //分页参数
        if(params.get("page")!=null) {
            this.page = Integer.parseInt(params.get("page").toString());
        }
        if(params.get("rows")!=null) {
            this.rows = Integer.parseInt(params.get("rows").toString());
        }
        this.remove("page");
        this.remove("rows");
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
