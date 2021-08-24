package com.example.leave.models.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "leaves")
public class Leave extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Employee manager;

    public Leave() {
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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

    @Override
    public String toString(){
        return  String.format("ID: %d, Start date: %s, End date: %s, Status: %s, Employee name: %s.", getId(),
                getStartDate().toString(),getEndDate().toString()
                ,getStatus().toString(),getEmployee().getFullName());
    }

}
