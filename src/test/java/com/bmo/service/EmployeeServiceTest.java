package com.bmo.service;

import com.bmo.dto.EmployeeDto;
import com.bmo.entity.EmployeeEntity;
import com.bmo.exception.EmployeeNotFoundException;
import com.bmo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeEntity testEntity;
    private EmployeeDto testDto;

    @BeforeEach
    void setUp() {
        testEntity = new EmployeeEntity(1L, "Aarif Diwan", "Engineering", 1L);
        testDto = new EmployeeDto(1L, "Aarif Diwan", "Engineering", 1L);
    }

    @Test
    void givenExistingEmployees_whenFetchingAll_thenReturnEmployeeSummaries() {
        // Given
        List<EmployeeEntity> entities = List.of(testEntity);
        when(employeeRepository.findAll()).thenReturn(entities);

        // When
        List<EmployeeDto> result = employeeService.getAllEmployees();

        // Then
        assertEquals(1, result.size());
        assertEquals(testDto, result.get(0));
        verify(employeeRepository).findAll();
    }

    @Test
    void givenEmployeeIdExists_whenFetchingEmployeeDetails_thenReturnEmployeeSummary() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEntity));

        // When
        EmployeeDto result = employeeService.getEmployeeById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testDto, result);
        verify(employeeRepository).findById(1L);
    }

    @Test
    void givenInvalidEmployeeId_whenFetchingEmployeeDetails_thenThrowNotFoundException() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(1L));
        verify(employeeRepository).findById(1L);
    }

    @Test
    void givenValidEmployeeDetails_whenCreatingEmployee_thenReturnSavedEmployeeSummary() {
        // Given
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(testEntity);

        // When
        EmployeeDto result = employeeService.createEmployee(testDto);

        // Then
        assertNotNull(result);
        assertEquals(testDto, result);
        verify(employeeRepository).save(any(EmployeeEntity.class));
    }

    @Test
    void givenExistingEmployee_whenUpdatingDetails_thenReturnUpdatedEmployeeSummary() {
        // Given
        EmployeeDto updateDto = new EmployeeDto(1L, "John Smith", "Finance", 1L);
        EmployeeEntity updatedEntity = new EmployeeEntity(1L, "John Smith", "Finance", 1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(updatedEntity);

        // When
        EmployeeDto result = employeeService.updateEmployee(1L, updateDto);

        // Then
        assertNotNull(result);
        assertEquals("John Smith", result.name());
        assertEquals("Finance", result.department());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(EmployeeEntity.class));
    }

    @Test
    void givenNonExistingEmployee_whenUpdatingDetails_thenThrowNotFoundException() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(1L, testDto));
        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).save(any(EmployeeEntity.class));
    }

    @Test
    void givenExistingEmployee_whenDeletingEmployee_thenRemoveSuccessfully() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        // When
        employeeService.deleteEmployee(1L);

        // Then
        verify(employeeRepository).existsById(1L);
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void givenNonExistingEmployee_whenDeletingEmployee_thenThrowNotFoundException() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(false);

        // When/Then
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(1L));
        verify(employeeRepository).existsById(1L);
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
