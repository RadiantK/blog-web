package com.mylog.member.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("존재하는 회원이 없습니다.");
    }
}
