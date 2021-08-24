package com.example.leave.service;

import com.example.leave.models.entity.Employee;

import java.io.IOException;

public interface LeaveService {
    void newLeave(Employee employee) throws IOException;

    void editLeave(Employee employee) throws IOException;

    void showLeave(Employee employee) throws Exception;
}
