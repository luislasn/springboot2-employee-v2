package projectengeneeringv2.springboot2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectengeneeringv2.springboot2.model.EmployeeUser;

public interface EmployeeUserRepository extends JpaRepository<EmployeeUser, Long> {

    EmployeeUser findByUsername(String name);

}
