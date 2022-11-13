package com.mylog.member.domain;

import com.mylog.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // 이메일

    private String password; // 비밀번호

    private String name; // 이름

    private String phone; // 전화번호

    @Enumerated(EnumType.STRING)
    private GenderType gender; // 성별

    @Embedded
    private Address address; // 주소

    @Enumerated(EnumType.STRING)
    private RoleType role; // 권한

    private Integer enabled; // 탈퇴 여부

    @Builder
    public Member(String email, String password, String name, String phone, GenderType gender,
                  Address address, RoleType role, Integer enabled) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.role = role;
        this.enabled = enabled;
    }

    public void editMember(String password, String name, String phone, GenderType gender) {
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

    public void removeMember() {
        this.enabled = 0;
    }
}
