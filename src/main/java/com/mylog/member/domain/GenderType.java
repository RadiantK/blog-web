package com.mylog.member.domain;

public enum GenderType {
    MALE("남자"), FEMALE("여자");

    private String value;

    GenderType(String value) {
        this.value = value;
    }
}
