package com.majlishekhidmat.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.majlishekhidmat.dto.ActivityCommentDto;
import com.majlishekhidmat.dto.ActivityDto;

public interface ActivityService {
    ActivityDto createActivity(String activityName, String description, String activityDate, MultipartFile imageFile, String createdBy);
    List<ActivityDto> getAllActivities();
    ActivityDto getActivityById(Long id);
    ActivityDto updateActivity(Long id, String activityName, String description, MultipartFile imageFile); // updated
    void deleteActivity(Long id);

    ActivityDto addReaction(Long activityId, String username, String reactionType);
    ActivityDto removeReaction(Long activityId, String username);

    // ✅ Updated for nested comments
    ActivityCommentDto addComment(Long activityId, String username, String comment, Long parentId);

    ActivityCommentDto updateComment(Long activityId, Long commentId, String username, String comment);

    void deleteComment(Long activityId, Long commentId, String username);
}
