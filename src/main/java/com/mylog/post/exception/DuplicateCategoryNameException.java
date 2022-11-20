package com.mylog.post.exception;

public class DuplicateCategoryNameException extends RuntimeException {

    public DuplicateCategoryNameException() {
        super("동일한 카테고리명이 이미 존재합니다.");
    }
}
