package com.majlishekhidmat.serviceimplV2;

import com.majlishekhidmat.dtoV2.FeedbackDto;
import com.majlishekhidmat.entityV2.Feedback;
import com.majlishekhidmat.repositoryV2.FeedbackRepository;
import com.majlishekhidmat.serviceV2.FeedbackService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public FeedbackDto createFeedback(FeedbackDto dto) {
        Feedback feedback = Feedback.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .message(dto.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        Feedback saved = feedbackRepository.save(feedback);
        return mapToDto(saved);
    }

    @Override
    public FeedbackDto updateFeedback(Long id, FeedbackDto dto) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));

        feedback.setName(dto.getName());
        feedback.setEmail(dto.getEmail());
        feedback.setMessage(dto.getMessage());

        Feedback updated = feedbackRepository.save(feedback);
        return mapToDto(updated);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public FeedbackDto getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        return mapToDto(feedback);
    }

    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private FeedbackDto mapToDto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        dto.setId(feedback.getId());
        dto.setName(feedback.getName());
        dto.setEmail(feedback.getEmail());
        dto.setMessage(feedback.getMessage());
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }
}
