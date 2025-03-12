package com.khrd.crudhomeworkone.model;

import lombok.Data;

import java.util.List;
@Data
public class PagedResponseListTicket {
    private List<Ticket> items;
    private PaginationInfo pagination;
}
