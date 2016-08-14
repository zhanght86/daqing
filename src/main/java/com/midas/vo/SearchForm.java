package com.midas.vo;

import org.apache.ibatis.session.RowBounds;

public class SearchForm {

    private int page = 1;    //页码
    private int rows = 10;    //每页显示行

    private String sort;
    private String order = "asc";

    private String keyword;

    //分页
    public RowBounds getRowBounds(){
        return new RowBounds(this.page, this.rows);
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
