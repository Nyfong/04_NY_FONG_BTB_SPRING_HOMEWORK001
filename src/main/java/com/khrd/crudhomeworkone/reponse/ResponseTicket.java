package com.khrd.crudhomeworkone.reponse;

import com.khrd.crudhomeworkone.model.Items;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ResponseTicket<T> {
    private boolean success;
    private String message;
    private String status;
    private T payload;
    private LocalDateTime timestamp;
}
