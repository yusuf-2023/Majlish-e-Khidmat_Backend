package com.majlishekhidmat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.majlishekhidmat.dto.ActivityCommentDto;
import com.majlishekhidmat.dto.ActivityDto;
import com.majlishekhidmat.service.ActivityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173" })
public class ActivityController {

    private final ActivityService activityService;

    // ------------------- Create Activity (Admin only)
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityDto> createActivity(
            @RequestParam("activityName") String activityName,
            @RequestParam("description") String description,
            @RequestParam("activityDate") String activityDate,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("createdBy") String createdBy
    ) {
        ActivityDto created = activityService.createActivity(activityName, description, activityDate, imageFile, createdBy);
        return ResponseEntity.ok(created);
    }

    // ------------------- Get all Activities (Public)
    @GetMapping("/all")
    public ResponseEntity<List<ActivityDto>> getAllActivities() {
        List<ActivityDto> list = activityService.getAllActivities();
        return ResponseEntity.ok(list);
    }

    // ------------------- Get Activity by ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<ActivityDto> getActivity(@PathVariable Long id) {
        ActivityDto dto = activityService.getActivityById(id);
        return ResponseEntity.ok(dto);
    }

    // ------------------- Update Activity (Admin only)
    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityDto> updateActivity(
            @PathVariable Long id,
            @RequestParam("activityName") String activityName,
            @RequestParam("description") String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        ActivityDto updated = activityService.updateActivity(id, activityName, description, imageFile);
        return ResponseEntity.ok(updated);
    }

    // ------------------- Delete Activity (Admin only)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.ok("Activity deleted successfully");
    }

    // ------------------- Add Reaction (User or Admin)
    @PostMapping("/react/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ActivityDto> addReaction(@PathVariable Long id,
                                                   @RequestParam String username,
                                                   @RequestParam String reactionType) {
        ActivityDto dto = activityService.addReaction(id, username, reactionType);
        return ResponseEntity.ok(dto);
    }

    // ------------------- Remove Reaction (User or Admin)
    @DeleteMapping("/react/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ActivityDto> removeReaction(@PathVariable Long id,
                                                      @RequestParam String username) {
        ActivityDto dto = activityService.removeReaction(id, username);
        return ResponseEntity.ok(dto);
    }

    // ------------------- Add Comment (User or Admin)
    @PostMapping("/comment/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ActivityCommentDto> addComment(@PathVariable Long id,
                                                         @RequestParam String username,
                                                         @RequestParam String comment,
                                                         @RequestParam(required = false) Long parentId) {
        ActivityCommentDto dto = activityService.addComment(id, username, comment, parentId);
        return ResponseEntity.ok(dto);
    }

    // ------------------- Update Comment (Owner or Admin)
    @PutMapping("/comment/{activityId}/{commentId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ActivityCommentDto> updateComment(@PathVariable Long activityId,
                                                            @PathVariable Long commentId,
                                                            @RequestParam String username,
                                                            @RequestParam String comment) {
        ActivityCommentDto dto = activityService.updateComment(activityId, commentId, username, comment);
        return ResponseEntity.ok(dto);
    }

    // ------------------- Delete Comment (Admin or Owner)
    @DeleteMapping("/comment/{activityId}/{commentId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteComment(@PathVariable Long activityId,
                                           @PathVariable Long commentId,
                                           @RequestParam String username) {
        activityService.deleteComment(activityId, commentId, username);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
