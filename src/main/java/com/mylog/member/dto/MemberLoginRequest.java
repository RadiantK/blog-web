package com.mylog.member.dto;

import lombok.Data;

@Data
public class MemberLoginRequest {

    private String email;
    private String password;
}
