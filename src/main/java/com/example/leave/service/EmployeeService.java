package com.example.leave.service;

import com.example.leave.models.entity.Employee;

import java.io.IOException;

public interface EmployeeService {
    Employee findByEmail(String email);

    void register() throws IOException;

    void login() throws IOException;

    Employee getLoggedUser();

    void editUser() throws Exception;

    void logout();
}
