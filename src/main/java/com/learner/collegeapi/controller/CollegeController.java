package com.learner.collegeapi.controller;

import com.learner.collegeapi.dto.ApiResponse;
import com.learner.collegeapi.dto.CollegeDto;
import com.learner.collegeapi.service.CollegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/colleges")
public class CollegeController {
    private final CollegeService collegeService;

    @PostMapping
    public ResponseEntity<ApiResponse<CollegeDto>> createCollege(@RequestBody CollegeDto collegeDto) {
        CollegeDto createdCollege = collegeService.createCollege(collegeDto);
        ApiResponse<CollegeDto> response = ApiResponse.success("College created successfully", createdCollege);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<CollegeDto>>> getAllColleges() {
        Iterable<CollegeDto> colleges = collegeService.getAllColleges();
        ApiResponse<Iterable<CollegeDto>> response = ApiResponse.success("Colleges fetched successfully", colleges);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CollegeDto>> getCollegeById(@PathVariable Long id) {
        CollegeDto college = collegeService.getCollegeById(id);
        ApiResponse<CollegeDto> response = ApiResponse.success("College fetched successfully", college);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CollegeDto>> updateCollege(@PathVariable Long id, @RequestBody CollegeDto collegeDto) {
        CollegeDto updatedCollege = collegeService.updateCollege(id, collegeDto);
        return ResponseEntity.ok(ApiResponse.success("College updated successfully", updatedCollege));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCollege(@PathVariable Long id) {
        collegeService.deleteCollege(id);
        return ResponseEntity.ok(ApiResponse.success("College deleted successfully", null));
    }
}
