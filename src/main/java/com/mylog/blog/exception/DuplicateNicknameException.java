package com.mylog.blog.exception;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException() {
        super("중복된 닉네임이 존재합니다.");
    }
}
