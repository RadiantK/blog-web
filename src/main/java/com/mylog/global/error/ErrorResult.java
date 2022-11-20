package com.mylog.global.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResult {

    private int code;
    private String message;
}
