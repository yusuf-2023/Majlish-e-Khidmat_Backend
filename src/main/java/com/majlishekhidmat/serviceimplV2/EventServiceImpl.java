package com.majlishekhidmat.serviceimplV2;



import com.majlishekhidmat.dtoV2.EventDto;
import com.majlishekhidmat.entityV2.Event;
import com.majlishekhidmat.repositoryV2.EventRepository;
import com.majlishekhidmat.serviceV2.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public EventDto createEvent(EventDto dto) {
        Event event = Event.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .date(dto.getDate())
                .description(dto.getDescription())
                .build();

        Event saved = eventRepository.save(event);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public EventDto updateEvent(Long id, EventDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setName(dto.getName());
        event.setLocation(dto.getLocation());
        event.setDate(dto.getDate());
        event.setDescription(dto.getDescription());

        eventRepository.save(event);
        dto.setId(event.getId());
        return dto;
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setLocation(event.getLocation());
        dto.setDate(event.getDate());
        dto.setDescription(event.getDescription());
        return dto;
    }

    @Override
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream().map(event -> {
            EventDto dto = new EventDto();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setLocation(event.getLocation());
            dto.setDate(event.getDate());
            dto.setDescription(event.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }
}
