package com.bmo.service;

import com.bmo.dto.EmployeeDto;
import com.bmo.entity.EmployeeEntity;
import com.bmo.exception.EmployeeNotFoundException;
import com.bmo.repository.EmployeeRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private EmployeeDto toDto(EmployeeEntity entity) {
        return new EmployeeDto(
            entity.getId(),
            entity.getName(),
            entity.getDepartment(),
            entity.getVersion()
        );
    }

    private EmployeeEntity toEntity(EmployeeDto dto) {
        return new EmployeeEntity(
            dto.id(),
            dto.name(),
            dto.department(),
            dto.version()
        );
    }

    private void updateEntityFromDto(EmployeeEntity entity, EmployeeDto dto) {
        entity.setName(dto.name());
        entity.setDepartment(dto.department());
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
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
