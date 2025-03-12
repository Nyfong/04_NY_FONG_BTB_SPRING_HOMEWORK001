package com.khrd.crudhomeworkone.model;

import lombok.Data;

@Data
public class PaginationInfo {
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}