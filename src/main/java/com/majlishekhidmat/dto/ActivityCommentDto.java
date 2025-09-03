package com.majlishekhidmat.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityCommentDto {
    private Long id;
    private String username;
    private String comment;
    private LocalDateTime commentedAt;

    // ✅ For nested replies
    private List<ActivityCommentDto> replies;
}
