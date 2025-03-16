package com.bmo.service;

import com.bmo.exception.EmployeeNotFoundException;
import com.bmo.model.Employee;
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

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = Employee.createEmployee("John Doe", "Engineering");
    }

    @Test
    void givenEmployeesInRepository_whenGetAllEmployees_thenReturnEmployeeList() {
        // Given
        List<Employee> employees = List.of(new Employee(1L, "John Doe", "IT", 1L));
        when(employeeRepository.findAll()).thenReturn(employees);

        // When
        List<Employee> result = employeeService.getAllEmployees();

        // Then
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).name());
        verify(employeeRepository).findAll();
    }

    @Test
    void givenEmployeeExists_whenGetEmployeeById_thenReturnEmployee() {
        // Given
        Long id = 1L;
        Employee employee = new Employee(id, "John Doe", "IT", 1L);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        // When
        Employee result = employeeService.getEmployeeById(id);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        verify(employeeRepository).findById(id);
    }

    @Test
    void givenEmployeeDoesNotExist_whenGetEmployeeById_thenThrowException() {
        // Given
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(id));
        verify(employeeRepository).findById(id);
    }

    @Test
    void givenValidEmployee_whenCreateEmployee_thenReturnSavedEmployee() {
        // Given
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        // When
        Employee result = employeeService.createEmployee(testEmployee);

        // Then
        assertNotNull(result);
        assertEquals(testEmployee.name(), result.name());
        assertEquals(testEmployee.department(), result.department());
        verify(employeeRepository).save(testEmployee);
    }

    @Test
    void givenExistingEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // Given
        Long id = 1L;
        Employee existingEmployee = new Employee(id, "John Doe", "IT", 1L);
        Employee updatedEmployee = new Employee(id, "John Smith", "Finance", 1L);
        
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // When
        Employee result = employeeService.updateEmployee(id, updatedEmployee);

        // Then
        assertNotNull(result);
        assertEquals("John Smith", result.name());
        assertEquals("Finance", result.department());
        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void givenNonExistingEmployee_whenUpdateEmployee_thenThrowException() {
        // Given
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EmployeeNotFoundException.class, 
            () -> employeeService.updateEmployee(id, testEmployee));
        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void givenExistingEmployee_whenDeleteEmployee_thenDeleteSuccessfully() {
        // Given
        Long id = 1L;
        when(employeeRepository.existsById(id)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(id);

        // When
        employeeService.deleteEmployee(id);

        // Then
        verify(employeeRepository).existsById(id);
        verify(employeeRepository).deleteById(id);
    }

    @Test
    void givenNonExistingEmployee_whenDeleteEmployee_thenThrowException() {
        // Given
        Long id = 1L;
        when(employeeRepository.existsById(id)).thenReturn(false);

        // When/Then
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(id));
        verify(employeeRepository).existsById(id);
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}