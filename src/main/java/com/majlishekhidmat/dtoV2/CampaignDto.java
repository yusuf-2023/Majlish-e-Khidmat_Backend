package com.majlishekhidmat.dtoV2;




import lombok.Data;

import java.time.LocalDate;

@Data
public class CampaignDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double targetAmount;
    private String status;
}

