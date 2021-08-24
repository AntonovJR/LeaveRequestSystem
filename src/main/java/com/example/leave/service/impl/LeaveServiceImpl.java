package com.example.leave.service.impl;

import com.example.leave.models.dto.NewLeaveDto;
import com.example.leave.models.entity.Employee;
import com.example.leave.models.entity.Leave;
import com.example.leave.models.entity.Status;
import com.example.leave.repository.LeaveRepository;
import com.example.leave.service.LeaveService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;


@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final BufferedReader reader;
    private final ModelMapper modelMapper;

    public LeaveServiceImpl(LeaveRepository leaveRepository,
                            ModelMapper modelMapper) {
        this.leaveRepository = leaveRepository;

        this.reader = new BufferedReader(new InputStreamReader(System.in));

        this.modelMapper = modelMapper;
    }

    @Override
    public void newLeave(Employee employee) throws IOException {
        System.out.println("Please enter from date in pattern dd/MM/yyyy: ");
        String startDate = reader.readLine();
        System.out.println("Please enter to date in pattern dd/MM/yyyy: ");
        String endDate = reader.readLine();

        NewLeaveDto newLeaveDto = new NewLeaveDto("PENDING", startDate, endDate, employee);
        try {
            Leave leave = modelMapper.map(newLeaveDto, Leave.class);
            leaveRepository.save(leave);
        } catch (Exception e) {
            System.out.println("----------------");
            System.out.println("Enter valid date in pattern dd/MM/yyyy");
            System.out.println("----------------");
            newLeave(employee);
        }


        System.out.println("----------------");
        System.out.println("Your leave is under review");
        System.out.println("----------------");

    }

    @Override
    public void showLeave(Employee employee) throws Exception {
        if (employee.getJobTitle().toString().equals("EMPLOYEE")) {
            Set<Leave> employeeLeaves = leaveRepository.getAllByEmployeeOrderByStartDate(employee);
            for (Leave s : employeeLeaves) {
                System.out.println(s.toString());
            }
        } else {
            while (true) {
                System.out.println("###############");
                System.out.printf("Please choose option:" +
                        "%n1.Your Leaves" +
                        "%n2.Employee Leaves" +
                        "%n3.All Leaves" +
                        "%n12.Back%n");
                System.out.println("###############");
                System.out.print("Option: ");

                int chosenOption = Integer.parseInt(reader.readLine());
                switch (chosenOption) {
                    case 1 -> managerLeaves(employee);
                    case 2 -> employeesLeaves(employee);
                    case 3 -> companyLeaves(employee);
                    case 12 -> {
                        return;
                    }
                    default -> System.out.println("Please enter valid option");


                }


            }
        }

    }

    private void companyLeaves(Employee employee) throws Exception {
        if (!employee.getJobTitle().toString().equals("CEO")) {
            System.out.println("----------------");
            System.out.println("You don`t have permission");
            System.out.println("----------------");
            showLeave(employee);
        }
        Set<Leave> employeeLeaves = leaveRepository.getAllLeaves();
        for (Leave s : employeeLeaves) {
            System.out.println(s.toString());
        }

    }

    private void employeesLeaves(Employee employee) {
        Set<Leave> employeeLeaves = leaveRepository.getAllByManager(employee.getId());
        for (Leave s : employeeLeaves) {
            System.out.println(s.toString());

        }
    }

    private void managerLeaves(Employee employee) {
        Set<Leave> employeeLeaves = leaveRepository.getAllByEmployeeOrderByStartDate(employee);
        for (Leave s : employeeLeaves) {
            System.out.println(s.toString());

        }
    }


    @Override

    public void editLeave(Employee employee) throws IOException {
        if (employee.getJobTitle().toString().equals("EMPLOYEE")) {
            System.out.println("----------------");
            System.out.println("You don`t have permission");
            System.out.println("----------------");
            return;
        }
        System.out.print("Please enter employee email:");
        String email = reader.readLine();

        Set<Leave> employeeLeaves;

        if (employee.getJobTitle().toString().equals("CEO")) {
            employeeLeaves = leaveRepository.getAllByEmployeeEmailAndStatusPendingOrOrderByStartDate(email, Status.PENDING);
        }else{
            employeeLeaves = leaveRepository.getAllPendingLeavesByManager(email, employee.getId(), Status.PENDING);
        }

        if (employeeLeaves.isEmpty()) {
            System.out.println("----------------");
            System.out.println("There are no pending leaves or you don`t have permission to edit");
            System.out.println("----------------");
        }
        for (Leave employeeLeave : employeeLeaves) {
            System.out.println(employeeLeave.toString());
            System.out.println("###############");
            System.out.printf("Please choose option:" +
                    "%n1.Approve" +
                    "%n2.Deny" +
                    "%n3.Delete" +
                    "%n12.Back%n");
            System.out.println("###############");
            System.out.print("Option: ");

            int chosenOption = Integer.parseInt(reader.readLine());
            switch (chosenOption) {
                case 1 -> approveLeave(employeeLeave);
                case 2 -> denyLeave(employeeLeave);
                case 3 -> deleteLeave(employeeLeave);
                case 12 -> {
                    return;
                }
                default -> System.out.println("Please enter valid option");


            }


        }

    }

    private void deleteLeave(Leave employeeLeave) throws IOException {
        System.out.println("Please write \"confirm\" to confirm:");
        String confirm = reader.readLine();
        if (confirm.equals("confirm")) {
            leaveRepository.delete(employeeLeave);
            System.out.println("----------------");
            System.out.printf("Successfully deleted leave%n");
            System.out.println("----------------");
        }
    }

    private void denyLeave(Leave employeeLeave) {
        employeeLeave.setStatus(Status.DENIED);
        leaveRepository.save(employeeLeave);
        System.out.println("----------------");
        System.out.println("Successfully denied employee leave");
        System.out.println("----------------");


    }

    private void approveLeave(Leave employeeLeave) {
        employeeLeave.setStatus(Status.APPROVED);
        leaveRepository.save(employeeLeave);
        System.out.println("----------------");
        System.out.println("Successfully approved employee leave");
        System.out.println("----------------");

    }


}






