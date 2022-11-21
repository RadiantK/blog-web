package com.mylog.post.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategorySaveRequest {

    @NotBlank
    private String name;
    private String email;
}
