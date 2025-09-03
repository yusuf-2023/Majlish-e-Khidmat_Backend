package com.majlishekhidmat.repository;

import com.majlishekhidmat.entity.Activity;
import com.majlishekhidmat.entity.ActivityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityCommentRepository extends JpaRepository<ActivityComment, Long> {
    List<ActivityComment> findByActivity(Activity activity);
}
