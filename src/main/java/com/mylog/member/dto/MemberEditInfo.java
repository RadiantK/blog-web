package com.mylog.member.dto;

import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class MemberEditInfo {

    private String email;

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

    public MemberEditInfo(String email, String name, String phone, GenderType gender) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

    public static MemberEditInfo of(Member member) {
        return new MemberEditInfo(
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                member.getGender());
    }

    public boolean isPasswordEqualToPasswordConfirm() {
        return password.equals(passwordConfirm);
    }
}
