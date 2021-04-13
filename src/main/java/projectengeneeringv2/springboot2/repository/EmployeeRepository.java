package projectengeneeringv2.springboot2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectengeneeringv2.springboot2.model.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByName(String name);

}
