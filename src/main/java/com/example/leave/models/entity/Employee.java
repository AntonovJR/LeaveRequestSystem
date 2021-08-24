package com.example.leave.models.entity;


import javax.persistence.*;
import java.util.Set;

@Entity(name = "employees")
public class Employee extends BaseEntity {
    @Column(name = "full_name")
    private String fullName;
    @Enumerated(EnumType.STRING)
    @Column(name = "job_title")
    private JobTitle jobTitle;
    private String email;
    private String password;
    @ManyToOne
    private Employee manager;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Leave> leaveSet;

    public Employee() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
