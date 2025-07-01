package com.learner.collegeapi.dto;

import com.learner.collegeapi.entity.College;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeDto {
    private String name;
    private String location;
    private Integer establishedYear;

    public CollegeDto(College college) {
        this.name = college.getName();
        this.location = college.getLocation();
        this.establishedYear = college.getEstablishedYear();
    }
}
