package com.bmo.controller;

import com.bmo.dto.EmployeeDto;
import com.bmo.exception.InvalidSortPropertyException;
import com.bmo.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import com.bmo.dto.PageResponseDto;


/**
 * REST Controller for handling employee-related HTTP requests.
 * Provides endpoints for CRUD operations with pagination and sorting support.
 * Implements OpenAPI documentation for API visibility.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    /**
     * Constructor injection of employee service.
     *
     * @param employeeService Service layer for employee operations
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves a paginated list of employees with sorting capabilities.
     * Supports sorting by multiple properties and directions.
     *
     * @param page Page number (0-based)
     * @param size Number of items per page
     * @param sort Sort criteria in format: property(,asc|desc)
     * @return ResponseEntity containing paginated employee list
     * @throws InvalidSortPropertyException if sort property is invalid
     */
    @GetMapping("/employees")  // Plural for collection
    @Operation(summary = "Get all employees with pagination")
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved the paginated list of employees",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponseDto.class))
    )
    public ResponseEntity<PageResponseDto<EmployeeDto>> getAllEmployees(
        @Parameter(description = "Page number (0-based)")
        @RequestParam(defaultValue = "0") int pageParam,
        @Parameter(description = "Number of items per page")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Sorting criteria in format: property(,asc|desc). Valid properties are: id, name, department")
        @RequestParam(required = false, defaultValue = "id,asc") String sort
    ) {
        try {
            List<String> validProperties = Arrays.asList("id", "name", "department", "version");
            
            String[] sortParams = sort.split(",");
            String property = sortParams[0].toLowerCase();
            
            if (!validProperties.contains(property)) {
                throw new InvalidSortPropertyException("Invalid sort property: " + property + 
                    ". Valid properties are: " + String.join(", ", validProperties));
            }
            
            Sort sortOrder;
            if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
                sortOrder = Sort.by(Sort.Direction.DESC, property);
            } else {
                sortOrder = Sort.by(Sort.Direction.ASC, property);
            }

            Page<EmployeeDto> page = employeeService.getAllEmployees(PageRequest.of(pageParam, size, sortOrder));
            return ResponseEntity.ok(PageResponseDto.from(page));
        } catch (IllegalArgumentException e) {
            throw new InvalidSortPropertyException("Invalid sort parameter format. Use: property,asc|desc");
        }
    }

    @GetMapping("/employee/{id}")  // Singular for single resource
    @Operation(summary = "Get employee by ID")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employee found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employee not found",
            content = @Content
        )
    })
    public ResponseEntity<EmployeeDto> getEmployeeById(
        @Parameter(description = "ID of the employee to retrieve") @PathVariable Long id
    ) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping("/employee")
    @Operation(summary = "Create new employee")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employee created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid employee data provided",
            content = @Content
        )
    })
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PutMapping("/employee/{id}")
    @Operation(summary = "Update employee")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employee updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employee not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Concurrent modification detected",
            content = @Content
        )
    })
    public ResponseEntity<EmployeeDto> updateEmployee(
        @PathVariable Long id,
        @Valid @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/employee/{id}")
    @Operation(summary = "Delete employee")
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Employee deleted successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employee not found",
            content = @Content
        )
    })
    public ResponseEntity<Void> deleteEmployee(
        @Parameter(description = "ID of the employee to delete") @PathVariable Long id
    ) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}