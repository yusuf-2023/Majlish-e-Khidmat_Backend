package com.majlishekhidmat.controller;

import com.majlishekhidmat.dto.ActivityDto;
import com.majlishekhidmat.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin("*") // Allow frontend calls
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ActivityDto create(@RequestBody ActivityDto dto) {
        return activityService.createActivity(dto);
    }

    @GetMapping
    public List<ActivityDto> getAll() {
        return activityService.getAllActivities();
    }

    @GetMapping("/{id}")
    public ActivityDto getById(@PathVariable Long id) {
        return activityService.getActivityById(id);
    }

    @PutMapping("/{id}")
    public ActivityDto update(@PathVariable Long id, @RequestBody ActivityDto dto) {
        return activityService.updateActivity(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        activityService.deleteActivity(id);
    }
}
