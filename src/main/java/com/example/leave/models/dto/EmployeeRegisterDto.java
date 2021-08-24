package com.example.leave.models.dto;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EmployeeRegisterDto {
    @Size(min = 3, message = "Username length must be more than two characters")
    private  String fullName;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$", message = "Enter valid password")
    private  String password;
    private  String confirmPassword;

    public EmployeeRegisterDto(String fullName, String password, String confirmPassword) {
        this.fullName = fullName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
