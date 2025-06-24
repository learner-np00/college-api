## What is an API?
An API (Application Programming Interface) is a set of rules and tools that allows different software applications to communicate with each other. It acts as an intermediary between different systems, enabling them to exchange data or perform functions without needing to understand each other’s internal workings. For example, an API can let a mobile app fetch data from a server or allow a website to integrate with a payment gateway.

## What is a REST API?
A REST API (Representational State Transfer API) is a type of API that follows the principles of REST, an architectural style for designing networked applications. REST APIs use standard HTTP methods (GET, POST, PUT, DELETE) to perform operations on resources, which are identified by URLs. They are stateless, meaning each request contains all the information needed to process it, and they typically return data in formats like JSON or XML.

## Key features of REST APIs:
- Stateless: Each request is independent.
- Resource-based: Operations are performed on resources (e.g., /colleges).
- HTTP methods: Uses GET (retrieve), POST (create), PUT (update), DELETE (delete).
- Scalable and flexible: Widely used due to simplicity and compatibility with web standards.

## What Do We Use in Spring Boot?
In Spring Boot, we typically use RESTful APIs — meaning we build APIs that follow the REST architecture using HTTP methods (GET, POST, PUT, DELETE) to handle CRUD operations on resources (like users, movies, products, etc.).


## CRUD API for Colleges
```zip
college-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── example/
│   │   │   │   │   ├── collegeapi/
│   │   │   │   │   │   ├── CollegeApiApplication.java
│   │   │   │   │   │   ├── config/
│   │   │   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   │   ├── CollegeDto.java
│   │   │   │   │   │   │   ├── ErrorDetail.java
│   │   │   │   │   │   │   ├── ApiResponse.java
│   │   │   │   │   │   ├── entity/
│   │   │   │   │   │   │   ├── College.java
│   │   │   │   │   │   ├── exception/
│   │   │   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   ├── CollegeRepository.java
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   ├── CollegeService.java
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   ├── CollegeController.java
│   │   │   │   │   │   ├── CollegeApiApplication.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   ├── pom.xml
```

#### 3.1. Project Setup (pom.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>college-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>college-api</name>
    <description>Spring Boot College API</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.0</version>
        <relativePath/>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 3.2. Application Configuration (application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/college_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

#### 3.3. Main Application (CollegeApiApplication.java)
```java
package com.example.collegeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CollegeApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollegeApiApplication.class, args);
    }
}
```

#### 3.4. Entity (College.java)
```java
package com.example.collegeapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private Integer establishedYear;
}
```

#### 3.5. DTOs
##### CollegeDto.java
```java
package com.example.collegeapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CollegeDto {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Location is mandatory")
    private String location;
    @Min(value = 1800, message = "Established year must be after 1800")
    private Integer establishedYear;
}
```

##### ApiResponse.java
```java
package com.example.collegeapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private List<ErrorDetail> errors;

    public ApiResponse(String status, String message, T data, List<ErrorDetail> errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data, null);
    }

    public static <T> ApiResponse<T> error(String message, List<ErrorDetail> errors) {
        return new ApiResponse<>("error", message, null, errors);
    }
}
```

##### ErrorDetail.java
```java
package com.example.collegeapi.dto;

import lombok.Data;

@Data
public class ErrorDetail {
    private String field;
    private String message;

    public ErrorDetail(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
```

#### 3.6. Exception (ResourceNotFoundException.java)
```java
package com.example.collegeapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

#### 3.7. Repository (CollegeRepository.java)
```java
package com.example.collegeapi.repository;

import com.example.collegeapi.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long> {
}
```

#### 3.8. Service (CollegeService.java)
```java
package com.example.collegeapi.service;

import com.example.collegeapi.dto.CollegeDto;
import com.example.collegeapi.entity.College;
import com.example.collegeapi.exception.ResourceNotFoundException;
import com.example.collegeapi.repository.CollegeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;

    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    public CollegeDto createCollege(CollegeDto collegeDto) {
        College college = new College();
        BeanUtils.copyProperties(collegeDto, college);
        College savedCollege = collegeRepository.save(college);
        CollegeDto result = new CollegeDto();
        BeanUtils.copyProperties(savedCollege, result);
        return result;
    }

    public List<CollegeDto> getAllColleges() {
        return collegeRepository.findAll().stream().map(college -> {
            CollegeDto dto = new CollegeDto();
            BeanUtils.copyProperties(college, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public CollegeDto getCollegeById(Long id) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College with ID " + id + " not found"));
        CollegeDto dto = new CollegeDto();
        BeanUtils.copyProperties(college, dto);
        return dto;
    }

    public CollegeDto updateCollege(Long id, CollegeDto collegeDto) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College with ID " + id + " not found"));
        BeanUtils.copyProperties(collegeDto, college, "id");
        College updatedCollege = collegeRepository.save(college);
        CollegeDto result = new CollegeDto();
        BeanUtils.copyProperties(updatedCollege, result);
        return result;
    }

    public void deleteCollege(Long id) {
        if (!collegeRepository.existsById(id)) {
            throw new ResourceNotFoundException("College with ID " + id + " not found");
        }
        collegeRepository.deleteById(id);
    }
}
```

#### 3.9. Controller (CollegeController.java)
```java
package com.example.collegeapi.controller;

import com.example.collegeapi.dto.ApiResponse;
import com.example.collegeapi.dto.CollegeDto;
import com.example.collegeapi.service.CollegeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colleges")
public class CollegeController {

    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CollegeDto>> createCollege(@Valid @RequestBody CollegeDto collegeDto) {
        CollegeDto createdCollege = collegeService.createCollege(collegeDto);
        return new ResponseEntity<>(ApiResponse.success("College created successfully", createdCollege), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CollegeDto>>> getAllColleges() {
        List<CollegeDto> colleges = collegeService.getAllColleges();
        return ResponseEntity.ok(ApiResponse.success("Colleges retrieved successfully", colleges));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CollegeDto>> getCollegeById(@PathVariable Long id) {
        CollegeDto college = collegeService.getCollegeById(id);
        return ResponseEntity.ok(ApiResponse.success("College retrieved successfully", college));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CollegeDto>> updateCollege(@PathVariable Long id, @Valid @RequestBody CollegeDto collegeDto) {
        CollegeDto updatedCollege = collegeService.updateCollege(id, collegeDto);
        return ResponseEntity.ok@ApiResponse.success("College updated successfully", updatedCollege));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCollege(@PathVariable Long id) {
        collegeService.deleteCollege(id);
        return new ResponseEntity<>(ApiResponse.success("College deleted successfully", null), HttpStatus.NO_CONTENT);
    }
}
```

#### 3.10. Global Exception Handler (GlobalExceptionHandler.java)
```java
package com.example.collegeapi.config;

import com.example.collegeapi.dto.ApiResponse;
import com.example.collegeapi.dto.ErrorDetail;
import com.example.collegeapi.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorDetail errorDetail = new ErrorDetail("id", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), List.of(errorDetail)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ErrorDetail(((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(ApiResponse.error("Validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        ErrorDetail errorDetail = new ErrorDetail("general", "An unexpected error occurred");
        return new ResponseEntity<>(ApiResponse.error("Internal server error", List.of(errorDetail)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

#### Explanation
Project Structure:
1. Entity (College.java): Represents the colleges table in the database with fields id, name, location, and establishedYear.
2. DTO (CollegeDto.java): Used to transfer data between client and server, with validation constraints (@NotBlank, @Min).
3. ApiResponse.java: Standard response wrapper for all API responses, with status, message, data, and errors.
4. ErrorDetail.java: Represents individual error details for validation or other errors.
5. Repository (CollegeRepository.java): JPA repository for database operations.
6. Service (CollegeService.java): Business logic for CRUD operations, converting between College and CollegeDto.
7. Controller (CollegeController.java): REST endpoints for /api/v1/colleges, returning ApiResponse with appropriate HTTP status codes.
8. GlobalExceptionHandler.java: Handles exceptions (e.g., ResourceNotFoundException, validation errors) and returns consistent error responses.

#### API Endpoints:
- POST /api/v1/colleges: Creates a new college (201 Created).
- GET /api/v1/colleges: Retrieves all colleges (200 OK).
- GET /api/v1/colleges/{id}: Retrieves a college by ID (200 OK).
- PUT /api/v1/colleges/{id}: Updates a college (200 OK).
- DELETE /api/v1/colleges/{id}: Deletes a college (204 No Content).

#### Response Examples:
1. Success (POST):
```json
{
  "status": "success",
  "message": "College created successfully",
  "data": {
    "id": 1,
    "name": "Example College",
    "location": "New York",
    "establishedYear": 1990
  },
  "errors": null
}
```

2. Error (Validation):
```json
{
  "status": "error",
  "message": "Validation failed",
  "data": null,
  "errors": [
    {
      "field": "name",
      "message": "Name is mandatory"
    }
  ]
}
```

#### Setup Instructions:
1. Configure PostgreSQL and update application.properties with your database credentials.
2. Run mvn spring-boot:run to start the application.
3. Test endpoints using tools like Postman or curl.
4. Database schema is automatically created via spring.jpa.hibernate.ddl-auto=update.

#### Additional Features:
- Validation: Ensures name and location are not blank, and establishedYear is after 1800.
- Error Handling: Consistent error responses for not found, validation errors, and unexpected exceptions.
- Lombok: Reduces boilerplate code for getters, setters, and constructors.
- Stateless: Each request is independent, adhering to REST principles.
