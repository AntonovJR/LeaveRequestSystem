package com.example.leave;

import com.example.leave.service.EmployeeService;
import com.example.leave.service.LeaveService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final EmployeeService employeeService;
    private final LeaveService leaveService;
    private final BufferedReader reader;

    public CommandLineRunnerImpl(EmployeeService employeeService, LeaveService leaveService) {
        this.employeeService = employeeService;
        this.leaveService = leaveService;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run(String... args) throws Exception {
        do {
            loginOrRegister();
        } while (employeeService.getLoggedUser()==null);

        showLoggedUserOptions();
    }

    public void showLoggedUserOptions() throws Exception {
        while (true) {
            System.out.println("###############");
            System.out.printf("Please choose option:" +
                    "%n1.New Leave" +
                    "%n2.Show Leave" +
                    "%n3.Edit Leave" +
                    "%n4.Edit User" +
                    "%n12.Logout%n");
            System.out.println("###############");
            System.out.print("Option: ");

            int chosenOption = Integer.parseInt(reader.readLine());
            switch (chosenOption) {
                case 1 -> leaveService.newLeave(employeeService.getLoggedUser());
                case 2 -> leaveService.showLeave(employeeService.getLoggedUser());
                case 3 -> leaveService.editLeave(employeeService.getLoggedUser());
                case 4 -> employeeService.editUser();
                case 12 -> {
                    employeeService.logout();
                    run();
                }
                default -> System.out.println("Please enter valid option");


            }


        }
    }

    public void loginOrRegister() throws IOException {
        System.out.println("###############");
        System.out.printf("Please choose option:%n1.Login%n2.Register%n");
        System.out.println("###############");
        System.out.print("Option: ");
        int chosenOption = Integer.parseInt(reader.readLine());
        switch (chosenOption) {
            case 1 -> employeeService.login();
            case 2 ->  employeeService.register();
            default -> System.out.printf("----------------%nPlease enter valid option%n----------------");
        }
    }
}





