package com.majlishekhidmat.serviceV2;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.majlishekhidmat.dtoV2.VolunteerDto;

public interface VolunteerService {
    VolunteerDto createVolunteer(VolunteerDto dto);
    VolunteerDto updateVolunteer(Long id, VolunteerDto dto);
    VolunteerDto updateVolunteer(Long id, VolunteerDto dto, MultipartFile profilePicture);
    void deleteVolunteer(Long id);
    VolunteerDto getVolunteerById(Long id);
    List<VolunteerDto> getAllVolunteers();
    String saveProfilePicture(MultipartFile file) throws IOException;
}
