package projectengeneeringv2.springboot2.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projectengeneeringv2.springboot2.model.Employee;
import projectengeneeringv2.springboot2.requests.EmployeePostRequestBody;
import projectengeneeringv2.springboot2.requests.EmployeePutRequestBody;
import projectengeneeringv2.springboot2.service.EmployeeService;
import projectengeneeringv2.springboot2.util.EmployeeCreator;
import projectengeneeringv2.springboot2.util.EmployeePostRequestBodyCreator;
import projectengeneeringv2.springboot2.util.EmployeePutRequestBodyCreator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeServiceMock;

    @BeforeEach
    void setup() {
        PageImpl<Employee> employeePage = new PageImpl<>(List.of(EmployeeCreator.createValidEmployee()));
        BDDMockito.when(this.employeeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(employeePage);

        BDDMockito.when(this.employeeServiceMock.listAllNonPageable())
                .thenReturn(List.of(EmployeeCreator.createValidEmployee()));

        BDDMockito.when(this.employeeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(EmployeeCreator.createValidEmployee()));

        BDDMockito.when(this.employeeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(EmployeeCreator.createValidEmployee());

        BDDMockito.when(this.employeeServiceMock.save(ArgumentMatchers.any(EmployeePostRequestBody.class)))
                .thenReturn(EmployeeCreator.createValidEmployee());

        BDDMockito.doNothing().when(this.employeeServiceMock).replace(ArgumentMatchers.any(EmployeePutRequestBody.class));

        BDDMockito.doNothing().when(this.employeeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("listAll Returns List Of Employees Inside Page Object When Successful")
    void listAll_ReturnsListOfEmployeesInsidePageObject_WhenSuccessful() {
        String expectedName = EmployeeCreator.createValidEmployee().getName();
        String expectedOccupation = EmployeeCreator.createValidEmployee().getOccupation();
        Integer expectedAge = EmployeeCreator.createValidEmployee().getAge();
        BigDecimal expectedSalary = EmployeeCreator.createValidEmployee().getSalary();

        Page<Employee> employeePage = this.employeeController.listAll(null).getBody();

        Assertions.assertThat(employeePage).isNotNull();
        Assertions.assertThat(employeePage.toList())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(employeePage.toList().get(0).getName()).isEqualTo(expectedName);
        Assertions.assertThat(employeePage.toList().get(0).getOccupation()).isEqualTo(expectedOccupation);
        Assertions.assertThat(employeePage.toList().get(0).getAge()).isEqualTo(expectedAge);
        Assertions.assertThat(employeePage.toList().get(0).getSalary()).isEqualTo(expectedSalary);
    }

    @Test
    @DisplayName("listAllNonPageable Returns List Of Employees When Successful")
    void listAllNonPageable_ReturnsListOfEmployees_WhenSuccessful() {
        String expectedName = EmployeeCreator.createValidEmployee().getName();
        String expectedOccupation = EmployeeCreator.createValidEmployee().getOccupation();
        Integer expectedAge = EmployeeCreator.createValidEmployee().getAge();
        BigDecimal expectedSalary = EmployeeCreator.createValidEmployee().getSalary();

        List<Employee> employees = this.employeeController.listAllNonPageable().getBody();

        Assertions.assertThat(employees)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(employees.get(0).getName()).isEqualTo(expectedName);
        Assertions.assertThat(employees.get(0).getOccupation()).isEqualTo(expectedOccupation);
        Assertions.assertThat(employees.get(0).getAge()).isEqualTo(expectedAge);
        Assertions.assertThat(employees.get(0).getSalary()).isEqualTo(expectedSalary);
    }

    @Test
    @DisplayName("findByName Returns List Of Employees When Successful")
    void findByName_ReturnsListOfEmployees_WhenSuccessful() {
        String expectedName = EmployeeCreator.createValidEmployee().getName();

        List<Employee> employees = this.employeeController.findByName("employee").getBody();

        Assertions.assertThat(employees)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(employees.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findBYName Returns List Of Empty When Employee Is Not Found")
    void findBYName_ReturnsListOfEmpty_WhenEmployeeIsNotFound() {
        BDDMockito.when(this.employeeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Employee> employees = this.employeeController.findByName("employee").getBody();

        Assertions.assertThat(employees)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById Returns Employee When Successful")
    void findById_ReturnsEmployee_WhenSuccessful() {
        Long expectedId = EmployeeCreator.createValidEmployee().getId();

        Employee employee = this.employeeController.findById(1L).getBody();

        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("save Returns Employee When Successful")
    void save_ReturnsEmployee_WhenSuccessful() {
        Employee employee = this.employeeController.save(EmployeePostRequestBodyCreator.createEmployeePostRequestBody()).getBody();
        Assertions.assertThat(employee).isNotNull().isEqualTo(EmployeeCreator.createValidEmployee());
    }

    @Test
    @DisplayName("replace Updates Employee When Successful")
    void replace_UpdatesEmployee_WhenSuccessful() {
        Assertions.assertThatCode(() -> this.employeeController.replace(EmployeePutRequestBodyCreator.createEmployeePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = this.employeeController.replace(EmployeePutRequestBodyCreator.createEmployeePutRequestBody());
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete Removes Employee When Successful")
    void delete_RemovesEmployee_WhenSuccessful() {
        Assertions.assertThatCode(() -> this.employeeController.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = this.employeeController.delete(1L);
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}