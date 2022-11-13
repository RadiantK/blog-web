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

    private String postcode; // 우편번호

    private String street; // 주소

    @Column(name = "address_detail")
    private String detail; // 상세주소
}
