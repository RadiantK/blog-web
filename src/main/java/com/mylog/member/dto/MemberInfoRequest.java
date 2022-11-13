package com.mylog.member.dto;

import com.mylog.member.domain.GenderType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class MemberInfoRequest {

    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\\-_=+]).{8,20}$")
    private String password;

    private String passwordConfirm;

    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,4}$")
    private String name;

    @NotBlank
    @Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}")
    private String phone; // 전화번호

    @NotNull
    private GenderType gender; // 성별

    public boolean isPasswordEqualToPasswordConfirm() {
        return password.equals(passwordConfirm);
    }
}
