package com.bmo.repository;

import com.bmo.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository interface for Employee entities.
 * Extends JpaRepository to inherit basic CRUD operations and pagination
 * support.
 * No custom methods needed as basic JPA functionality covers all requirements.
 */
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
}
