package com.midas.vo;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> {

    private Integer total;
    
    private List<T> rows;

    public SearchResult() {
        this.total = 0;
        this.rows = new ArrayList<T>();
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

	public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
