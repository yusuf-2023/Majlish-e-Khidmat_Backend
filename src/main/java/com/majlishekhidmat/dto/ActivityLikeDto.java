package com.majlishekhidmat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLikeDto {
    private Long id;
    private String username;
    private String reactionType;
    private LocalDateTime likedAt;
}