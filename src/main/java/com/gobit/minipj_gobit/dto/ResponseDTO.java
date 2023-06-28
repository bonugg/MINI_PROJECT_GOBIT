package com.gobit.minipj_gobit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseDTO<T> {
    private List<T> items;
    private T item;
    private String errorMessage;
    private int statusCode;
    private LocalDateTime timestamp;

}
