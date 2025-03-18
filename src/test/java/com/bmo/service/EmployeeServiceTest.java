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
import org.springframework.data.domain.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

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

    @Test
    void givenExistingEmployees_whenFetchingAllWithPagination_thenReturnPagedEmployeeSummaries() {
        // Given
        List<EmployeeEntity> employees = List.of(
            testEntity,
            new EmployeeEntity(2L, "John Doe", "HR", 1L)
        );
        Page<EmployeeEntity> pagedEntities = new PageImpl<>(
            employees,
            PageRequest.of(0, 10, Sort.by("id").ascending()),
            2
        );
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(pagedEntities);

        // When
        Page<EmployeeDto> result = employeeService.getAllEmployees(
            PageRequest.of(0, 10, Sort.by("id").ascending())
        );

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(10, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getContent().size());
        
        // Verify first employee
        assertEquals(1L, result.getContent().get(0).id());
        assertEquals("Aarif Diwan", result.getContent().get(0).name());
        assertEquals("Engineering", result.getContent().get(0).department());
        
        // Verify second employee
        assertEquals(2L, result.getContent().get(1).id());
        assertEquals("John Doe", result.getContent().get(1).name());
        assertEquals("HR", result.getContent().get(1).department());
        
        // Verify repository was called with correct parameters
        verify(employeeRepository).findAll(any(Pageable.class));
    }

    @Test
    void givenNoEmployees_whenFetchingAllWithPagination_thenReturnEmptyPage() {
        // Given
        Page<EmployeeEntity> emptyPage = new PageImpl<>(
            List.of(),
            PageRequest.of(0, 10),
            0
        );
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // When
        Page<EmployeeDto> result = employeeService.getAllEmployees(PageRequest.of(0, 10));

        // Then
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertTrue(result.getContent().isEmpty());
        verify(employeeRepository).findAll(any(Pageable.class));
    }

    @Test
    void givenEmployees_whenFetchingWithCustomPageSize_thenReturnCorrectPageSize() {
        // Given
        List<EmployeeEntity> employees = List.of(testEntity);
        Page<EmployeeEntity> pagedEntities = new PageImpl<>(
            employees,
            PageRequest.of(0, 5),
            1
        );
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(pagedEntities);

        // When
        Page<EmployeeDto> result = employeeService.getAllEmployees(PageRequest.of(0, 5));

        // Then
        assertEquals(5, result.getSize());
        assertEquals(1, result.getContent().size());
        verify(employeeRepository).findAll(any(Pageable.class));
    }

    @Test
    void givenConcurrentUpdates_whenUpdatingEmployee_thenThrowOptimisticLockingException() throws InterruptedException {
        // Given
        EmployeeEntity originalEntity = new EmployeeEntity(1L, "Original Name", "Engineering", 1L);
        originalEntity.setVersion(1L);  // Set initial version
        
        // Simulate first read
        when(employeeRepository.findById(1L))
            .thenReturn(Optional.of(originalEntity));
        
        // Simulate concurrent update that increases version
        when(employeeRepository.save(any(EmployeeEntity.class)))
            .thenAnswer(invocation -> {
                EmployeeEntity savedEntity = invocation.getArgument(0);
                if (savedEntity.getVersion() != 1L) {
                    throw new ObjectOptimisticLockingFailureException(EmployeeEntity.class, savedEntity.getId());
                }
                savedEntity.setVersion(2L);
                return savedEntity;
            });

        // When/Then
        // First update should succeed
        EmployeeDto firstUpdate = new EmployeeDto(1L, "First Update", "Engineering", 1L);
        assertDoesNotThrow(() -> employeeService.updateEmployee(1L, firstUpdate));

        // Second concurrent update should fail
        EmployeeDto secondUpdate = new EmployeeDto(1L, "Second Update", "Engineering", 1L);
        assertThrows(ObjectOptimisticLockingFailureException.class, 
            () -> employeeService.updateEmployee(1L, secondUpdate));

        // Verify repository calls
        verify(employeeRepository, times(2)).findById(1L);
        verify(employeeRepository, times(2)).save(any(EmployeeEntity.class));
    }

}
