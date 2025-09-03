package com.majlishekhidmat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.majlishekhidmat.entity.Activity;
import com.majlishekhidmat.entity.ActivityLike;

public interface ActivityLikeRepository extends JpaRepository<ActivityLike, Long> {

    // Check if a user already liked the activity
    boolean existsByActivityAndUsername(Activity activity, String username);

    // Find like by activity and username
    Optional<ActivityLike> findByActivityAndUsername(Activity activity, String username);

    // Find all likes for an activity
    List<ActivityLike> findByActivity(Activity activity);
}
