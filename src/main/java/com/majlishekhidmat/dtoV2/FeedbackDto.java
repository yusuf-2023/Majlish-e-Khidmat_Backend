package com.majlishekhidmat.dtoV2;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDto {
    private Long id;
    private String name;
    private String email;
    private String message;
    private LocalDateTime createdAt;
}

