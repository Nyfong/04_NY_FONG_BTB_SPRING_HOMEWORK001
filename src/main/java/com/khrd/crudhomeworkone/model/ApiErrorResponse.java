package com.khrd.crudhomeworkone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse <T> {
    private String type;
    private String title;
    private int status;
    private String instance;
    private Instant timestamp;
    private T errors;
}
