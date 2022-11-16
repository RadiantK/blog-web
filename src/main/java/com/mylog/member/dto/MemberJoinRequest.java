package com.mylog.member.dto;

import com.mylog.member.domain.GenderType;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class MemberJoinRequest {

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email; // 이메일

    @Pattern(regexp = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\\-_=+]).{8,20}$")
    private String password; // 비밀번호

    @NotBlank
    private String passwordConfirm;

    @Pattern(regexp = "^[가-힣]{2,5}")
    private String name; // 이름

    @Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}")
    private String phone; // 전화번호

    @NotNull
    private GenderType gender; // 성별

    @Pattern(regexp = "^[0-9]{1,}$")
    private String postcode; // 우편번호

    @NotEmpty
    private String street; // 주소

    private String detail; // 상세주소

    public boolean isPasswordEqualToPasswordConfirm() {
        return password.equals(passwordConfirm);
    }
}
