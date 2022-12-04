package com.mylog.member.domain;

import com.mylog.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email; // 이메일

    @Column(nullable = false, length = 200)
    private String password; // 비밀번호

    @Column(nullable = false, length = 30)
    private String name; // 이름

    @Column(length = 13)
    private String phone; // 전화번호

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private GenderType gender; // 성별

    @Embedded
    private Address address; // 주소

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
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
