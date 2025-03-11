package com.khrd.crudhomeworkone.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseTicket<T> {
    private boolean success;
    private String message;
    private T payload;
}
