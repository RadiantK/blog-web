package com.mylog.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {

    @Column(length = 20)
    private String postcode; // 우편번호

    @Column(length = 200)
    private String street; // 주소

    @Column(name = "address_detail", length = 100)
    private String detail; // 상세주소
}
