package com.example.leave.repository;

import com.example.leave.models.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Employee findByEmail(String email);

    Employee findAllByEmailAndPassword(String email, String password);

    Employee getByEmail(String email);




}
