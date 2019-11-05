package com.frame.core.sql;

import java.util.List;

public class Pager<T> {
    // 默认页码
    public final static int DEFAULT_PAGE_INDEX = 1;
    // 默认分页大小
    public final static int DEFAULT_PAGE_SIZE = 10;

    private int pageSize = DEFAULT_PAGE_SIZE;// 每页显示的记录数
    private int pageIndex;// 当前第几页
    private long totalRowCount;
    private int totalPageCount;
    private List<T> data;
    private String sort;

    public Pager(){
        this.pageIndex = DEFAULT_PAGE_INDEX;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Pager(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex!=null?pageIndex:DEFAULT_PAGE_INDEX;
        this.pageSize = pageSize!=null?pageSize:DEFAULT_PAGE_SIZE;

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageNumber(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(long totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
