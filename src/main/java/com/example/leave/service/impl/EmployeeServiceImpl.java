package com.example.leave.service.impl;

import com.example.leave.models.dto.EmployeeLoginDto;
import com.example.leave.models.dto.EmployeeRegisterDto;
import com.example.leave.models.entity.Employee;
import com.example.leave.models.entity.JobTitle;
import com.example.leave.repository.EmployeeRepository;
import com.example.leave.service.EmployeeService;
import com.example.leave.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private Employee employee;
    private final EmployeeRepository employeeRepository;
    private final BufferedReader reader;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ValidationUtil validationUtil,
                               ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;

        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Employee findByEmail(String email) {
        return this.employeeRepository.findByEmail(email);
    }


    @Override
    public void login() throws IOException {
        System.out.print("Please enter email: ");
        String email = reader.readLine();
        System.out.print("Please enter password: ");
        String password = reader.readLine();
        EmployeeLoginDto userLoginDto = new EmployeeLoginDto(email, password);

        Employee user = employeeRepository.findByEmail(userLoginDto.getEmail());
        if (user == null) {
            System.out.println("----------------");
            System.out.println("Email not found");
            System.out.println("----------------");
            return;

        }
        user = employeeRepository.findAllByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword());
        if (user == null) {
            System.out.println("----------------");
            System.out.println("Wrong password");
            System.out.println("----------------");
            return;
        }


        employee = user;
        System.out.println("----------------");
        System.out.printf("Successfully logged %s%n", employee.getFullName());
        System.out.println("----------------");

    }

    @Override
    public Employee getLoggedUser() {
        return this.employee;
    }


    public void editUser() throws Exception {
        Employee employeeToEdit = employee;
        if (!employee.getJobTitle().toString().equals("EMPLOYEE")) {
            System.out.print("Enter employee email: ");
            String email = reader.readLine();
            employeeToEdit = employeeRepository.getByEmail(email);
            if (employeeToEdit == null) {
                System.out.println("----------------");
                System.out.println("No such employee");
                System.out.println("----------------");
                editUser();
            }
            if (!employeeToEdit.getEmail().equals(employee.getEmail())
                    && !employeeToEdit.getManager().getEmail().equals(employee.getEmail())
                    &&!employee.getJobTitle().toString().equals("CEO")) {
                System.out.println("----------------");
                System.out.println("You don`t have permission");
                System.out.println("----------------");
                return;
            }

        }


        while (true) {
            System.out.println("###############");
            System.out.printf("Please choose option:" +
                    "%n1.Change name" +
                    "%n2.Change password" +
                    "%n3.Change email" +
                    "%n4.Chane job title" +
                    "%n5.Change manager id" +
                    "%n6.Delete employee" +
                    "%n12.Back%n");
            System.out.println("###############");
            System.out.print("Option: ");
            int chosenOption = Integer.parseInt(reader.readLine());
            switch (chosenOption) {
                case 1 -> changeName(employeeToEdit);
                case 2 -> changePassword(employeeToEdit);
                case 3 -> changeEmail(employeeToEdit);
                case 4 -> changeJobTitle(employeeToEdit);
                case 5 -> changeManagerId(employeeToEdit);
                case 6 -> deleteUser(employeeToEdit);
                case 12 -> {
                    return;
                }
            }

        }
    }

    private void changeManagerId(Employee employeeToEdit) throws Exception {
        if (employee.getJobTitle().toString().equals("EMPLOYEE")) {
            System.out.println("----------------");
            System.out.println("You don`t have permission");
            System.out.println("----------------");
            editUser();
        }
        System.out.print("Enter new manager email: ");
        String managerEmail = reader.readLine();
        Employee newManager = findByEmail(managerEmail);
        if (newManager.getJobTitle().toString().equals("MANAGER") || newManager.getJobTitle().toString().equals("CEO")) {
            employeeToEdit.setManager(employeeRepository.getByEmail(managerEmail));
            employeeRepository.save(employeeToEdit);

            System.out.println("----------------");
            System.out.printf("Successfully changed manager of %s%n", employeeToEdit.getFullName());
            System.out.println("----------------");
        } else {
            System.out.println("----------------");
            System.out.println("Employee is not a manager");
            System.out.println("----------------");
        }
    }

    private void changeJobTitle(Employee employeeToEdit) throws Exception {
        if (employee.getJobTitle().toString().equals("EMPLOYEE") || employee.getJobTitle().toString().equals("MANAGER")) {
            System.out.println("----------------");
            System.out.println("You don`t have permission");
            System.out.println("----------------");
            editUser();
        }
        System.out.println("Enter new job title:");
        String jobTitle = reader.readLine();
        employeeToEdit.setJobTitle(JobTitle.valueOf(jobTitle.toUpperCase(Locale.ROOT)));
        employeeRepository.save(employeeToEdit);
        System.out.println("----------------");
        System.out.printf("Successfully changed job title of %s%n", employeeToEdit.getFullName());
        System.out.println("----------------");

    }

    private void changeEmail(Employee employeeToEdit) throws Exception {
        if (employee.getJobTitle().toString().equals("EMPLOYEE")) {
            System.out.println("----------------");
            System.out.println("You don`t have permission");
            System.out.println("----------------");
            editUser();
        }
        System.out.print("Enter new email: ");
        String email = reader.readLine();
        employeeToEdit.setEmail(email);
        employeeRepository.save(employeeToEdit);
        System.out.println("----------------");
        System.out.printf("Successfully changed email of %s%n", employeeToEdit.getFullName());
        System.out.println("----------------");

    }

    private void changePassword(Employee employeeToEdit) throws Exception {

        if (employee.getEmail().equals(employeeToEdit.getEmail())) {
            System.out.print("Enter old password: ");
            String oldPassword = reader.readLine();

            if (!employee.getPassword().equals(oldPassword)) {
                System.out.println("----------------");
                System.out.println("Invalid password. Call manager for help.");
                System.out.println("----------------");
                editUser();
            }
        }

        System.out.print("Enter new password: ");
        String password = reader.readLine();
        if(employeeToEdit.getPassword().equals(password)){
            System.out.println("----------------");
            System.out.println("New password can`t be same as the old password");
            System.out.println("----------------");
            editUser();
        }
        if (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$")) {
            employeeToEdit.setPassword(password);
            employeeRepository.save(employeeToEdit);
            System.out.println("----------------");
            System.out.printf("Successfully changed password of %s%n", employeeToEdit.getFullName());
            System.out.println("----------------");
        } else {
            System.out.println("----------------");
            System.out.println("Enter valid password");
            System.out.println("----------------");
        }
    }

    private void changeName(Employee employeeToEdit) throws Exception {

        System.out.print("Enter new name: ");
        String name = reader.readLine();
        employeeToEdit.setFullName(name);
        employeeRepository.save(employeeToEdit);
        System.out.println("----------------");
        System.out.printf("Successfully changed name of %s%n", employeeToEdit.getFullName());
        System.out.println("----------------");

    }

    public void deleteUser(Employee employeeToEdit) throws Exception {
        if(employeeToEdit.getJobTitle().toString().equals("CEO")){
            System.out.println("----------------");
            System.out.printf("This is not possible. %n");
            System.out.println("----------------");
            editUser();
        }
        System.out.print("Please write \"confirm\" to confirm: ");
        String confirm = reader.readLine();
        if (confirm.equals("confirm")) {
            Employee employeeToDelete = employee;
            if (!employee.getJobTitle().toString().equals("EMPLOYEE")) {
                employeeToDelete = employeeToEdit;
            }
            employeeRepository.delete(employeeToDelete);
            System.out.println("----------------");
            System.out.printf("Successfully deleted employee %s%n", employeeToDelete.getFullName());
            System.out.println("----------------");

        }
    }



    public void logout() {
        System.out.println("----------------");
        System.out.println("Successfully logged out");
        System.out.println("----------------");
        this.employee = null;
        System.exit(0);

    }


    @Override
    public void register() throws IOException {
        System.out.println("Please enter full name: ");
        String name = reader.readLine();
        System.out.println("Please enter password at least 6 symbols, must contain at least 1 uppercase, " +
                "1 lowercase letter and 1 digit: ");
        String password = reader.readLine();
        System.out.println("Please confirm password: ");
        String confirmPassword = reader.readLine();
        EmployeeRegisterDto userRegisterDto = new EmployeeRegisterDto(name, password, confirmPassword);
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("----------------");
            System.out.println("Wrong confirm password");
            System.out.println("----------------");
            return;
        }

        Set<ConstraintViolation<EmployeeRegisterDto>> violation =
                validationUtil.violation(userRegisterDto);
        if (!violation.isEmpty()) {
            System.out.println("----------------");
            violation
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            System.out.println("----------------");
            return;
        }
        Employee user = modelMapper.map(userRegisterDto, Employee.class);
        user.setEmail(user.getFullName().toLowerCase(Locale.ROOT).replace(" ", ".") + "@company.com");
        user.setManager(employeeRepository.getById(1L));
        if (checkEmail(user.getEmail())) {
            registerEmployee(user);

        } else {
            user.setEmail(user.getFullName().toLowerCase(Locale.ROOT).replace(" ", ".") +
                    employeeRepository.count() + "@company.com");
            registerEmployee(user);
        }


    }

    private void registerEmployee(Employee user) {
        employeeRepository.save(user);
        if (user.getId() == 1L) {
            user.setJobTitle(JobTitle.CEO);
        } else {
            user.setJobTitle(JobTitle.EMPLOYEE);
        }
        employeeRepository.save(user);
        System.out.println("----------------");
        System.out.printf("%s was registered%n", user.getFullName());
        System.out.printf("Your company email is %s%n", user.getEmail());
        System.out.println("----------------");
    }

    private boolean checkEmail(String email) {
        return employeeRepository.findByEmail(email) == null;
    }

}
