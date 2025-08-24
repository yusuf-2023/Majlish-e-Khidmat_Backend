package com.majlishekhidmat.serviceimpl;

import com.majlishekhidmat.dto.ActivityDto;
import com.majlishekhidmat.entity.Activity;
import com.majlishekhidmat.repository.ActivityRepository;
import com.majlishekhidmat.service.ActivityService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    private ActivityDto mapToDto(Activity activity) {
        return ActivityDto.builder()
                .id(activity.getId())
                .activityName(activity.getActivityName())
                .description(activity.getDescription())
                .activityDate(activity.getActivityDate())
                .build();
    }

    private Activity mapToEntity(ActivityDto dto) {
        return Activity.builder()
                .id(dto.getId())
                .activityName(dto.getActivityName())
                .description(dto.getDescription())
                .activityDate(dto.getActivityDate())
                .build();
    }

    @Override
    public ActivityDto createActivity(ActivityDto dto) {
        Activity activity = mapToEntity(dto);
        return mapToDto(activityRepository.save(activity));
    }

    @Override
    public List<ActivityDto> getAllActivities() {
        return activityRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityDto getActivityById(Long id) {
        return activityRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
    }

    @Override
    public ActivityDto updateActivity(Long id, ActivityDto dto) {
        Activity existing = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        existing.setActivityName(dto.getActivityName());
        existing.setDescription(dto.getDescription());
        existing.setActivityDate(dto.getActivityDate());

        return mapToDto(activityRepository.save(existing));
    }

    @Override
    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }
}
