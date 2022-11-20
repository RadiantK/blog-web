package com.mylog.member.dto;

import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponse {

    private String email;
    private RoleType role;
    private String blogName;
}
