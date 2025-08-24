package com.majlishekhidmat.serviceV2;




import com.majlishekhidmat.dtoV2.EventDto;
import java.util.List;

public interface EventService {
    EventDto createEvent(EventDto dto);
    EventDto updateEvent(Long id, EventDto dto);
    void deleteEvent(Long id);
    EventDto getEventById(Long id);
    List<EventDto> getAllEvents();
}
