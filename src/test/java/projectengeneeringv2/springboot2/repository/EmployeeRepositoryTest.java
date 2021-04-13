package projectengeneeringv2.springboot2.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projectengeneeringv2.springboot2.model.Employee;
import projectengeneeringv2.springboot2.util.EmployeeCreator;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for employee repository")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("save Persists Employee When Successful")
    void save_PersistsEmployee_WhenSuccessful() {
        Employee employeeToBeSaved = EmployeeCreator.createEmployeeToBeSaved();
        Employee employeeSaved = this.employeeRepository.save(employeeToBeSaved);
        String expectedName = employeeSaved.getName();
        String expectedOccupation = employeeSaved.getOccupation();
        Integer expectedAge = employeeSaved.getAge();
        BigDecimal expectedSalary = employeeSaved.getSalary();
        Assertions.assertThat(employeeSaved).isNotNull();
        Assertions.assertThat(employeeSaved.getId()).isNotNull();
        Assertions.assertThat(employeeSaved.getName()).isEqualTo(expectedName);
        Assertions.assertThat(employeeSaved.getOccupation()).isEqualTo(expectedOccupation);
        Assertions.assertThat(employeeSaved.getAge()).isEqualTo(expectedAge);
        Assertions.assertThat(employeeSaved.getSalary()).isEqualTo(expectedSalary);
    }

    @Test
    @DisplayName("save Updates Employee When Successful")
    void save_UpdatesEmployee_WhenSuccessful() {
        Employee employeeToBeSaved = EmployeeCreator.createEmployeeToBeSaved();
        Employee employeeSaved = this.employeeRepository.save(employeeToBeSaved);
        employeeSaved.setName("Cacilda Cordeiro");
        employeeSaved.setOccupation("Diretoria");
        employeeSaved.setAge(57);
        employeeSaved.setSalary(BigDecimal.valueOf(5890.00));
        Employee employeeUpdated = this.employeeRepository.save(employeeSaved);
        Assertions.assertThat(employeeUpdated).isNotNull();
        Assertions.assertThat(employeeUpdated.getId()).isNotNull();
        Assertions.assertThat(employeeUpdated.getName()).isEqualTo(employeeSaved.getName());
        Assertions.assertThat(employeeUpdated.getOccupation()).isEqualTo(employeeSaved.getOccupation());
        Assertions.assertThat(employeeUpdated.getAge()).isEqualTo(employeeSaved.getAge());
        Assertions.assertThat(employeeUpdated.getSalary()).isEqualTo(employeeSaved.getSalary());
    }

    @Test
    @DisplayName("delete Removes Employee When Successful")
    void delete_RemovesEmployee_WhenSuccessful() {
        Employee employeeToBeSaved = EmployeeCreator.createEmployeeToBeSaved();
        Employee employeeSaved = this.employeeRepository.save(employeeToBeSaved);
        this.employeeRepository.delete(employeeSaved);
        Optional<Employee> employeeOptional = this.employeeRepository.findById(employeeSaved.getId());
        Assertions.assertThat(employeeOptional).isEmpty();
    }

    @Test
    @DisplayName("findByName Returns List Of Employee When Successful")
    void findByName_ReturnsListOfEmployee_WhenSuccessful() {
        Employee employeeToBeSaved = EmployeeCreator.createEmployeeToBeSaved();
        Employee employeeSaved = this.employeeRepository.save(employeeToBeSaved);
        String expectedName = employeeSaved.getName();
        List<Employee> employees = this.employeeRepository.findByName(expectedName);
        Assertions.assertThat(employees)
                .isNotEmpty()
                .contains(employeeSaved);
    }

    @Test
    @DisplayName("findByName Returns Empty List When Employee Is Not Found")
    void findByName_ReturnsEmptyList_WhenEmployeeIsNotFound() {
        List<Employee> employees = this.employeeRepository.findByName("test");
        Assertions.assertThat(employees).isEmpty();
    }

    @Test
    @DisplayName("save Throw Constraint Violation Exception When Name Or Occupation Is Empty")
    void save_ThrowConstraintViolationException_WhenNameOrOccupationIsEmpty() {
        Employee employee = new Employee();
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.employeeRepository.save(employee))
                .withMessageContaining("The employee name cannot be empty")
                .withMessageContaining("The employee occupation cannot be empty");
    }

}