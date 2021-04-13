package projectengeneeringv2.springboot2.service;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projectengeneeringv2.springboot2.exception.BadRequestException;
import projectengeneeringv2.springboot2.model.Employee;
import projectengeneeringv2.springboot2.repository.EmployeeRepository;
import projectengeneeringv2.springboot2.util.EmployeeCreator;
import projectengeneeringv2.springboot2.util.EmployeePostRequestBodyCreator;
import projectengeneeringv2.springboot2.util.EmployeePutRequestBodyCreator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<Employee> employeePage = new PageImpl<>(List.of(EmployeeCreator.createValidEmployee()));
        BDDMockito.when(employeeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(employeePage);

        BDDMockito.when(employeeRepositoryMock.findAll())
                .thenReturn(List.of(EmployeeCreator.createValidEmployee()));

        BDDMockito.when(employeeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(EmployeeCreator.createValidEmployee()));

        BDDMockito.when(employeeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(EmployeeCreator.createValidEmployee()));

        BDDMockito.when(employeeRepositoryMock.save(ArgumentMatchers.any()))
                .thenReturn(EmployeeCreator.createValidEmployee());

        BDDMockito.doNothing().when(employeeRepositoryMock).delete(ArgumentMatchers.any(Employee.class));
    }

    @Test
    @DisplayName("listAllNonPageable Returns List Of Anime When Successful")
    void listAll_ReturnsListOfEmployeesInsidePageObject_WhenSuccessful() {
        String expectedName = EmployeeCreator.createValidEmployee().getName();
        String expectedOccupation = EmployeeCreator.createValidEmployee().getOccupation();
        Integer expectedAge = EmployeeCreator.createValidEmployee().getAge();
        BigDecimal expectedSalary = EmployeeCreator.createValidEmployee().getSalary();

        Page<Employee> employeePage = this.employeeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(employeePage).isNotNull();
        Assertions.assertThat(employeePage.toList())
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

        List<Employee> employees = this.employeeService.listAllNonPageable();

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
    @DisplayName("findByName Returns List Of Employee When Successful")
    void findByName_ReturnsListOfEmployee_WhenSuccessful() {
        String expectedName = EmployeeCreator.createValidEmployee().getName();

        List<Employee> employees = this.employeeService.findByName("employee");

        Assertions.assertThat(employees)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(employees.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName Returns Empty List Of Employee When Is Not Found")
    void findByName_ReturnsEmptyListOfEmployee_WhenIsNotFound() {
        BDDMockito.when(employeeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Employee> employees = this.employeeService.findByName("employee");

        Assertions.assertThat(employees)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException Returns Employee When Successful")
    void findByIdOrThrowBadRequestException_ReturnsEmployee_WhenSuccessful() {
        Long expectedId = EmployeeCreator.createValidEmployee().getId();

        Employee employee = this.employeeService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException ThrowsBadRequestException When Employee Is Not Found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenEmployeeIsNotFound() {
        BDDMockito.when(employeeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> employeeService.findByIdOrThrowBadRequestException(1L));
    }

    @Test
    @DisplayName("save Returns Employee When Successful")
    void save_ReturnsEmployee_WhenSuccessful() {
        Employee employee = employeeService.save(EmployeePostRequestBodyCreator.createEmployeePostRequestBody());
        Assertions.assertThat(employee).isNotNull().isEqualTo(EmployeeCreator.createValidEmployee());
    }

    @Test
    @DisplayName("replace Updates Employee When Successful")
    void replace_UpdatesEmployee_WhenSuccessful() {
        Assertions.assertThatCode(() -> employeeService.replace(EmployeePutRequestBodyCreator.createEmployeePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete Removes Employee When Successful")
    void delete_RemovesEmployee_WhenSuccessful() {
        Assertions.assertThatCode(() -> employeeService.delete(1L))
                .doesNotThrowAnyException();
    }

}