package com.bmo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing an employee in the database.
 * Uses JPA annotations for ORM mapping and Lombok for reducing boilerplate code.
 * Implements optimistic locking using @Version annotation.
 */
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {
    /**
     * Unique identifier for the employee.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the employee.
     * Cannot be null in the database.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Department where the employee works.
     * Cannot be null in the database.
     */
    @Column(nullable = false)
    private String department;

    /**
     * Version number for optimistic locking.
     * Automatically managed by JPA.
     */
    @Version
    private Long version;
}
