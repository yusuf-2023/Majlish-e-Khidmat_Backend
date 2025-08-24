package com.majlishekhidmat.dtoV2;





import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String name;
    private String location;
    private LocalDateTime date;
    private String description;
}

