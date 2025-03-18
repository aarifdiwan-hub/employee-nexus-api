package com.bmo.controller;

import com.bmo.config.TestSecurityConfig;
import com.bmo.dto.EmployeeDto;
import com.bmo.exception.EmployeeNotFoundException;
import com.bmo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@WebMvcTest(EmployeeController.class)
@Import(TestSecurityConfig.class)
class EmployeeControllerTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDto testEmployee;

    // Helper method to add basic auth to any request
    private MockHttpServletRequestBuilder securedRequest(MockHttpServletRequestBuilder request) {
        return request.with(httpBasic(USERNAME, PASSWORD));
    }

    @BeforeEach
    void setUp() {
        testEmployee = new EmployeeDto(1L, "Foo", "Engineering", 1L);
    }

    @Test
    void givenEmployeesExist_whenFetchingAllWithPagination_thenReturnPagedEmployeeSummaries() throws Exception {
        // Given
        Page<EmployeeDto> pagedResponse = new PageImpl<>(
            List.of(
                testEmployee,
                new EmployeeDto(2L, "Foo two", "HR", 1L)
            ),
            PageRequest.of(0, 10, Sort.by("id").ascending()),
            2
        );
        when(employeeService.getAllEmployees(any(Pageable.class))).thenReturn(pagedResponse);

        // When/Then
        mockMvc.perform(securedRequest(get("/api/v1/employees"))
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,asc")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].name", is("Foo")))
            .andExpect(jsonPath("$.content[1].id", is(2)))
            .andExpect(jsonPath("$.content[1].name", is("Foo two")))
            .andExpect(jsonPath("$.metadata.totalElements", is(2)))
            .andExpect(jsonPath("$.metadata.totalPages", is(1)))
            .andExpect(jsonPath("$.metadata.pageSize", is(10)))
            .andExpect(jsonPath("$.metadata.pageNumber", is(0)))
            .andExpect(jsonPath("$.metadata.first", is(true)))
            .andExpect(jsonPath("$.metadata.last", is(true)));

        verify(employeeService).getAllEmployees(any(Pageable.class));
    }

    @Test
    void givenInvalidSortProperty_whenFetchingAllWithPagination_thenReturnBadRequest() throws Exception {
        // When/Then
        mockMvc.perform(securedRequest(get("/api/v1/employees"))
                .param("page", "0")
                .param("size", "10")
                .param("sort", "invalid_property,asc")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("Invalid sort property")))
            .andExpect(jsonPath("$.status", is(400)));

        verify(employeeService, never()).getAllEmployees(any(Pageable.class));
    }



    @Test
    void givenEmployeeIdExists_whenFetchingEmployeeDetails_thenReturnEmployeeSummary() throws Exception {
        // Given
        when(employeeService.getEmployeeById(1L)).thenReturn(testEmployee);

        // When/Then
        mockMvc.perform(securedRequest(get("/api/v1/employees/1"))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Foo")))
            .andExpect(jsonPath("$.department", is("Engineering")));

        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    void givenEmployeeDoesNotExist_whenFetchingEmployeeDetails_thenReturnNotFound() throws Exception {
        // Given
        when(employeeService.getEmployeeById(1L))
                .thenThrow(new EmployeeNotFoundException("Employee not found with id: 1"));

        // When/Then
        mockMvc.perform(securedRequest(get("/api/v1/employees/1"))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    void givenValidEmployeeDetails_whenCreatingEmployee_thenReturnCreatedEmployeeSummary() throws Exception {
        // Given
        EmployeeDto newEmployee = new EmployeeDto(null, "Foo", "Engineering", null);
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(testEmployee);

        // When/Then
        mockMvc.perform(securedRequest(post("/api/v1/employees"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Foo")))
            .andExpect(jsonPath("$.department", is("Engineering")));

        verify(employeeService).createEmployee(any(EmployeeDto.class));
    }

    @Test
    void givenInvalidEmployeeDetails_whenCreatingEmployee_thenReturnBadRequest() throws Exception {
        // Given
        EmployeeDto invalidEmployee = new EmployeeDto(null, "", "", null);

        // When/Then
        mockMvc.perform(securedRequest(post("/api/v1/employees"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
            .andExpect(status().isBadRequest());

        verify(employeeService, never()).createEmployee(any(EmployeeDto.class));
    }

    @Test
    void givenExistingEmployee_whenUpdatingDetails_thenReturnUpdatedEmployeeSummary() throws Exception {
        // Given
        EmployeeDto updateEmployee = new EmployeeDto(1L, "Foo Updated", "IT", 1L);
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).thenReturn(updateEmployee);

        // When/Then
        mockMvc.perform(securedRequest(put("/api/v1/employees/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Foo Updated")))
            .andExpect(jsonPath("$.department", is("IT")));

        verify(employeeService).updateEmployee(eq(1L), any(EmployeeDto.class));
    }

    @Test
    void givenNonExistingEmployee_whenUpdatingDetails_thenReturnNotFound() throws Exception {
        // Given
        EmployeeDto updateEmployee = new EmployeeDto(1L, "Foo Updated", "IT", 1L);
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class)))
                .thenThrow(new EmployeeNotFoundException("Employee not found with id: 1"));

        // When/Then
        mockMvc.perform(securedRequest(put("/api/v1/employees/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)))
            .andExpect(status().isNotFound());

        verify(employeeService).updateEmployee(eq(1L), any(EmployeeDto.class));
    }

    @Test
    void givenExistingEmployee_whenDeletingEmployee_thenReturnNoContent() throws Exception {
        // Given
        doNothing().when(employeeService).deleteEmployee(1L);

        // When/Then
        mockMvc.perform(securedRequest(delete("/api/v1/employees/1")))
            .andExpect(status().isNoContent());

        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    void givenNonExistingEmployee_whenDeletingEmployee_thenReturnNotFound() throws Exception {
        // Given
        doThrow(new EmployeeNotFoundException("Employee not found with id: 1"))
                .when(employeeService).deleteEmployee(1L);

        // When/Then
        mockMvc.perform(securedRequest(delete("/api/v1/employees/1")))
            .andExpect(status().isNotFound());

        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    void givenConcurrentModification_whenUpdatingDetails_thenReturnConflict() throws Exception {
        // Given
        EmployeeDto updateEmployee = new EmployeeDto(1L, "Foo Updated", "IT", 1L);
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(EmployeeDto.class, 1L));

        // When/Then
        mockMvc.perform(securedRequest(put("/api/v1/employees/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)))
            .andExpect(status().isConflict());

        verify(employeeService).updateEmployee(eq(1L), any(EmployeeDto.class));
    }

}
