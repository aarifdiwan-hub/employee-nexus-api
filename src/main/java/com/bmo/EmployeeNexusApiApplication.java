package com.bmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Employee Nexus API application.
 * This Spring Boot application provides REST endpoints for employee management
 * with features like pagination, sorting, and optimistic locking.
 *
 * @author Aarif Diwan
 * @version 1.0.0
 */
@SpringBootApplication
public class EmployeeNexusApiApplication {

    /**
     * Bootstraps and launches the Spring Boot application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(EmployeeNexusApiApplication.class, args);
    }
}