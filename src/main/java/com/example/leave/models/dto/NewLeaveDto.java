package com.example.leave.models.dto;

import com.example.leave.models.entity.Employee;

public class NewLeaveDto {

    private  String status;
    private  String startDate;
    private  String endDate;
    private  Employee employee;
    private  Employee manager;

    public NewLeaveDto(String status, String startDate, String endDate, Employee employee) {
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employee = employee;
        this.manager = employee.getManager();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
