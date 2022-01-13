package com.example.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    private String message;
    private Object Data;

    public BaseResponse(String message) {
        this.message = message;
    }

    public BaseResponse(Object data) {
        Data = data;
    }
}
