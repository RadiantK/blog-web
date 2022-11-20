package com.mylog.global.common;

import lombok.Getter;

@Getter
public class Pagination {

    private int page;
    private int startPage;
    private int endPage;
    private int totalPage;

    public Pagination(int currPage, int totalPage) {
        this.page = currPage;
        this.endPage = (int) Math.ceil(currPage / 5.0) * 5;
        this.startPage = endPage - 4;
        this.totalPage = totalPage;

        if (endPage > totalPage) {
            endPage = totalPage;
        }
    }
}
