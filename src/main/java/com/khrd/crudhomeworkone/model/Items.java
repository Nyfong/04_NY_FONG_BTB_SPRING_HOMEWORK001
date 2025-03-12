package com.khrd.crudhomeworkone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Items <T>{
    private T item;
}
