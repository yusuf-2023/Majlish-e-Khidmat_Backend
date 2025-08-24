package com.majlishekhidmat.service;

import com.majlishekhidmat.dto.ActivityDto;
import java.util.List;

public interface ActivityService {
    ActivityDto createActivity(ActivityDto dto);
    List<ActivityDto> getAllActivities();
    ActivityDto getActivityById(Long id);
    ActivityDto updateActivity(Long id, ActivityDto dto);
    void deleteActivity(Long id);
}
