package com.mylog.comment.exception;

public class WrongCommentPasswordException extends RuntimeException {

    public WrongCommentPasswordException() {
        super("댓글 비밀번호가 일치하지 않습니다.");
    }
}
