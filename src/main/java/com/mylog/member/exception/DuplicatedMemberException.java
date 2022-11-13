package com.mylog.member.exception;

public class DuplicatedMemberException extends RuntimeException {

    public DuplicatedMemberException() {
        super("중복된 회원이 존재합니다.");
    }
}
