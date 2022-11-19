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

    public static MemberLoginResponse of(Member member) {
        if(member.getBlog() == null || member.getBlog().getName() == null) {
            return new MemberLoginResponse(
                    member.getEmail(),
                    member.getRole(),
                    null
            );
        }

        return new MemberLoginResponse(
                member.getEmail(),
                member.getRole(),
                member.getBlog().getName()
        );
    }
}
