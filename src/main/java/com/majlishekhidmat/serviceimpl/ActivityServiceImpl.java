package com.majlishekhidmat.serviceimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.majlishekhidmat.dto.ActivityCommentDto;
import com.majlishekhidmat.dto.ActivityDto;
import com.majlishekhidmat.dto.ActivityLikeDto;
import com.majlishekhidmat.entity.Activity;
import com.majlishekhidmat.entity.ActivityComment;
import com.majlishekhidmat.entity.ActivityLike;
import com.majlishekhidmat.repository.ActivityCommentRepository;
import com.majlishekhidmat.repository.ActivityLikeRepository;
import com.majlishekhidmat.repository.ActivityRepository;
import com.majlishekhidmat.service.ActivityService;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityCommentRepository commentRepository;

    @Autowired
    private ActivityLikeRepository likeRepository;

    @Override
    public ActivityDto createActivity(String activityName, String description, String activityDate, MultipartFile imageFile, String createdBy) {
        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/activities");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                imagePath = "/uploads/activities/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Activity activity = Activity.builder()
                .activityName(activityName)
                .description(description)
                .activityDate(LocalDateTime.parse(activityDate))
                .imagePath(imagePath)
                .createdBy(createdBy)
                .build();
        activity = activityRepository.save(activity);
        return mapToDto(activity);
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
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        return mapToDto(activity);
    }

    @Override
    public ActivityDto updateActivity(Long id, String activityName, String description, MultipartFile imageFile) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setActivityName(activityName);
        activity.setDescription(description);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/activities");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                activity.setImagePath("/uploads/activities/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        activity = activityRepository.save(activity);
        return mapToDto(activity);
    }

    @Override
    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }

    @Override
    public ActivityDto addReaction(Long activityId, String username, String reactionType) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        ActivityLike existingReaction = likeRepository.findByActivityAndUsername(activity, username).orElse(null);
        if (existingReaction != null) {
            existingReaction.setReactionType(reactionType);
            likeRepository.save(existingReaction);
        } else {
            ActivityLike newReaction = ActivityLike.builder()
                    .activity(activity)
                    .username(username)
                    .reactionType(reactionType)
                    .likedAt(LocalDateTime.now())
                    .build();
            likeRepository.save(newReaction);
        }
        return mapToDto(activity);
    }

    @Override
    public ActivityDto removeReaction(Long activityId, String username) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        ActivityLike existingReaction = likeRepository.findByActivityAndUsername(activity, username).orElse(null);
        if (existingReaction != null) {
            likeRepository.delete(existingReaction);
        }
        return mapToDto(activity);
    }

    // ✅ addComment with parentId (nested replies)
    @Override
    public ActivityCommentDto addComment(Long activityId, String username, String commentText, Long parentId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        ActivityComment parentComment = null;
        if (parentId != null) {
            parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }

        ActivityComment comment = ActivityComment.builder()
                .activity(activity)
                .username(username)
                .comment(commentText)
                .parent(parentComment)
                .createdAt(LocalDateTime.now())
                .build();

        comment = commentRepository.save(comment);
        return mapToCommentDto(comment);
    }

    // ✅ updateComment implementation
    @Override
    public ActivityCommentDto updateComment(Long activityId, Long commentId, String username, String updatedText) {
        activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        ActivityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUsername().equals(username) && !username.equalsIgnoreCase("admin")) {
            throw new RuntimeException("Unauthorized to update comment");
        }

        comment.setComment(updatedText);
        commentRepository.save(comment);

        return mapToCommentDto(comment);
    }

    // ✅ deleteComment with check
    @Override
    public void deleteComment(Long activityId, Long commentId, String username) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        ActivityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUsername().equals(username) && !username.equalsIgnoreCase("admin")) {
            throw new RuntimeException("Unauthorized to delete comment");
        }

        commentRepository.delete(comment);
    }

    private ActivityDto mapToDto(Activity activity) {
        List<ActivityCommentDto> comments = commentRepository.findByActivity(activity)
                .stream()
                .filter(c -> c.getParent() == null) // only top-level
                .map(this::mapToCommentDto)
                .collect(Collectors.toList());

        List<ActivityLikeDto> reactions = likeRepository.findByActivity(activity)
                .stream()
                .map(this::mapToLikeDto)
                .collect(Collectors.toList());

        return ActivityDto.builder()
                .id(activity.getId())
                .activityName(activity.getActivityName())
                .description(activity.getDescription())
                .activityDate(activity.getActivityDate())
                .imagePath(activity.getImagePath())
                .totalLikes(reactions.size())
                .comments(comments)
                .reactions(reactions)
                .createdBy(activity.getCreatedBy())
                .build();
    }

    private ActivityCommentDto mapToCommentDto(ActivityComment comment) {
        return ActivityCommentDto.builder()
                .id(comment.getId())
                .username(comment.getUsername())
                .comment(comment.getComment())
                .commentedAt(comment.getCreatedAt())
                .replies(
                        comment.getReplies() != null
                                ? comment.getReplies().stream().map(this::mapToCommentDto).collect(Collectors.toList())
                                : null
                )
                .build();
    }

    private ActivityLikeDto mapToLikeDto(ActivityLike like) {
        return ActivityLikeDto.builder()
                .id(like.getId())
                .username(like.getUsername())
                .reactionType(like.getReactionType())
                .likedAt(like.getLikedAt())
                .build();
    }
}
