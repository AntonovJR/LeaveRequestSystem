package com.example.leave.repository;

import com.example.leave.models.entity.Employee;
import com.example.leave.models.entity.Leave;
import com.example.leave.models.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {

    Set<Leave> getAllByEmployeeOrderByStartDate(Employee employee);

    @Query("select l from leaves l where l.manager.id = :id ")
    Set<Leave> getAllByManager(@Param(value = "id") Long id);

    @Query("select l from leaves l order by l.employee.id, l.startDate")
    Set<Leave> getAllLeaves();

    @Query("select l from leaves l where l.employee.email =:email and l.status = :status order by l.startDate")
    Set<Leave> getAllByEmployeeEmailAndStatusPendingOrOrderByStartDate(@Param(value = "email") String email, @Param(value = "status") Status status);

    @Query("select l from leaves l where l.employee.email =:email and l.manager.id = :id and l.status = :status order by l.employee.id, l.startDate")
    Set<Leave> getAllPendingLeavesByManager(@Param(value = "email") String email, @Param(value = "id") Long id, @Param(value = "status") Status status );
}
