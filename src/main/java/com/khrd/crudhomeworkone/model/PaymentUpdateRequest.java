package com.khrd.crudhomeworkone.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
public class PaymentUpdateRequest {

    private List<Long> ticketIds;
    private Boolean paymentStatus;

}