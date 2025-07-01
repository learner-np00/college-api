package com.learner.collegeapi.service;

import com.learner.collegeapi.dto.CollegeDto;
import com.learner.collegeapi.entity.College;
import com.learner.collegeapi.repository.CollegeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollegeService {
    private final CollegeRepository collegeRepository;

    public CollegeDto createCollege(CollegeDto collegeDto) {
        log.info("Creating college with name: {}", collegeDto.getName());
        College college = new College();
        college.setName(collegeDto.getName());
        college.setLocation(collegeDto.getLocation());
        college.setEstablishedYear(collegeDto.getEstablishedYear());

        collegeRepository.save(college);
        return new CollegeDto(college);
    }

    public List<CollegeDto> getAllColleges() {
        log.info("Fetching all colleges");
        List<College> colleges = collegeRepository.findAll();
        return colleges.stream()
                .map(CollegeDto::new)
                .toList();
    }

    public CollegeDto getCollegeById(Long id) {
        log.info("Fetching college with id: {}", id);
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("College not found with id: " + id));
        return new CollegeDto(college);
    }

    public void deleteCollege(Long id) {
        log.info("Deleting college with id: {}", id);
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("College not found with id: " + id));
        collegeRepository.delete(college);
    }

    public CollegeDto updateCollege(Long id, CollegeDto collegeDto) {
        log.info("Updating college with id: {}", id);
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("College with ID " + id + " not found"));
        college.setName(collegeDto.getName());
        college.setLocation(collegeDto.getLocation());
        college.setEstablishedYear(collegeDto.getEstablishedYear());

        collegeRepository.save(college);
        return new CollegeDto(college);
    }
}
