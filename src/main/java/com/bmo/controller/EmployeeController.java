package com.bmo.controller;

import com.bmo.dto.EmployeeDto;
import com.bmo.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
        summary = "Get all employees",
        description = "Retrieves a list of all employees in the system"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved the list of employees",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class))
    )
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Operation(
        summary = "Get employee by ID",
        description = "Retrieves an employee by their ID"
    )
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
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(
        @Parameter(description = "ID of the employee to retrieve") @PathVariable Long id
    ) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Operation(
        summary = "Create new employee",
        description = "Creates a new employee in the system"
    )
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
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @Operation(
        summary = "Update employee",
        description = "Updates an existing employee's information"
    )
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
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
        @PathVariable Long id,
        @Valid @RequestBody EmployeeDto employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @Operation(
        summary = "Delete employee",
        description = "Deletes an employee from the system"
    )
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
        @Parameter(description = "ID of the employee to delete") @PathVariable Long id
    ) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}