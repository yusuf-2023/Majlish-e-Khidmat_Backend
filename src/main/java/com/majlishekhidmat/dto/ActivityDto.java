package com.majlishekhidmat.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String imagePath;
    private String createdBy;

    private Integer totalLikes;

    private List<ActivityLikeDto> reactions;

    private List<ActivityCommentDto> comments;
}