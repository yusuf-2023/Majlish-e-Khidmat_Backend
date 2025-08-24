package com.majlishekhidmat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDto {
    private Long id;
    private String activityName;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
private LocalDateTime activityDate;

}
