package com.bmo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public record Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id,
    
    String name,
    
    String department,
    
    @Version
    Long version
) {
    // Factory method for creating new employees
    public static Employee createEmployee(String name, String department) {
        return new Employee(null, name, department, null);
    }
}