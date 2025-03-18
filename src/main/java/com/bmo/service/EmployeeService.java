package com.bmo.service;

import com.bmo.dto.EmployeeDto;
import com.bmo.entity.EmployeeEntity;
import com.bmo.exception.EmployeeNotFoundException;
import com.bmo.repository.EmployeeRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service layer for employee-related business logic.
 * Handles data transformation between DTOs and entities.
 * Implements transactional operations for data consistency.
 */
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor injection of employee repository.
     *
     * @param employeeRepository JPA repository for employee data access
     */
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Converts an employee entity to DTO.
     *
     * @param entity Employee entity to convert
     * @return DTO representation of the employee
     */
    private EmployeeDto toDto(EmployeeEntity entity) {
        return new EmployeeDto(
            entity.getId(),
            entity.getName(),
            entity.getDepartment(),
            entity.getVersion()
        );
    }

    /**
     * Converts an employee DTO to entity.
     *
     * @param dto DTO to convert
     * @return Entity representation of the employee
     */
    private EmployeeEntity toEntity(EmployeeDto dto) {
        return new EmployeeEntity(
            dto.id(),
            dto.name(),
            dto.department(),
            dto.version()
        );
    }

    /**
     * Updates an existing entity with DTO data.
     *
     * @param entity Entity to update
     * @param dto DTO containing new data
     */
    private void updateEntityFromDto(EmployeeEntity entity, EmployeeDto dto) {
        entity.setName(dto.name());
        entity.setDepartment(dto.department());
    }

    public Page<EmployeeDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(this::toDto);
    }

    public EmployeeDto getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        EmployeeEntity entity = toEntity(employeeDto);
        EmployeeEntity savedEntity = employeeRepository.save(entity);
        return toDto(savedEntity);
    }

    @Transactional
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        return employeeRepository.findById(id)
                .map(entity -> {
                    updateEntityFromDto(entity, employeeDto);
                    return employeeRepository.save(entity);
                })
                .map(this::toDto)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}
