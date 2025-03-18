package com.bmo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for Employee data.
 * Implements validation constraints and OpenAPI documentation.
 * Uses Java Record for immutable data  representation.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Employee Data Transfer Object")
public record EmployeeDto(

    @Schema(description = "Unique identifier of the employee",accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Name of the employee")
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    @Schema(description = "Department of the employee")
    @NotBlank(message = "Department is required")
    @Size(min = 2, max = 50, message = "Department must be between 2 and 50 characters")
    String department,

    @Schema(description = "Version number for optimistic locking",accessMode = Schema.AccessMode.READ_ONLY)
    Long version
) {}