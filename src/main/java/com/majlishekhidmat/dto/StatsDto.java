package com.majlishekhidmat.dto;

import lombok.Data;

@Data
public class StatsDto {
    private Long id;
    private Long volunteers;
    private Long donations;
    private Long communities; // Ensure this is Long, not int
}
