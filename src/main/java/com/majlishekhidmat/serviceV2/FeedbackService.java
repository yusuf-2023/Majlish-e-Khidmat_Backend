package com.majlishekhidmat.serviceV2;

import com.majlishekhidmat.dtoV2.FeedbackDto;
import java.util.List;

public interface FeedbackService {

    FeedbackDto createFeedback(FeedbackDto dto);

    FeedbackDto updateFeedback(Long id, FeedbackDto dto);

    void deleteFeedback(Long id);

    FeedbackDto getFeedbackById(Long id);

    List<FeedbackDto> getAllFeedbacks();
}
