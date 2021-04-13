package projectengeneeringv2.springboot2.integration;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import projectengeneeringv2.springboot2.model.Employee;
import projectengeneeringv2.springboot2.model.EmployeeUser;
import projectengeneeringv2.springboot2.repository.EmployeeRepository;
import projectengeneeringv2.springboot2.repository.EmployeeUserRepository;
import projectengeneeringv2.springboot2.requests.EmployeePostRequestBody;
import projectengeneeringv2.springboot2.util.EmployeeCreator;
import projectengeneeringv2.springboot2.util.EmployeePostRequestBodyCreator;
import projectengeneeringv2.springboot2.wrapper.PageableResponse;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeControllerIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testResTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testResTemplateRoleAdmin;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeUserRepository employeeUserRepository;

    private static final EmployeeUser USER = EmployeeUser.builder()
            .name("Employee User")
            .password("{bcrypt}$2a$10$A6j1yhA6EbDEDRrGkONyVeJIlqxlKaYFmVRhdJsMb9Eks8YQC6aGC")
            .username("employee")
            .authorities("ROLE_USER")
            .build();

    private static final EmployeeUser ADMIN = EmployeeUser.builder()
            .name("Luis Neto")
            .password("{bcrypt}$2a$10$A6j1yhA6EbDEDRrGkONyVeJIlqxlKaYFmVRhdJsMb9Eks8YQC6aGC")
            .username("luis")
            .authorities("ROLE_ADMIN,ROLE_USER")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("employee", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }
        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("luis", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    @DisplayName("listAll Returns List Of Employee Inside Page Object When Successful")
    void listAll_ReturnsListOfEmployeeInsidePageObject_WhenSuccessful() {
        Employee savedEmployee = this.employeeRepository.save(EmployeeCreator.createEmployeeToBeSaved());
        employeeUserRepository.save(USER);

        String expectedName = savedEmployee.getName();
        String expectedOccupation = savedEmployee.getOccupation();
        Integer expectedAge = savedEmployee.getAge();
        BigDecimal expectedSalary = savedEmployee.getSalary();

        PageableResponse<Employee> employeePage = testResTemplateRoleUser.exchange("/employees", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Employee>>() {
                }).getBody();

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
    @DisplayName("listAllNonPageable Returns Employee When Successful")
    void listAllNonPageable_ReturnsEmployee_WhenSuccessful() {
        Employee savedEmployee = this.employeeRepository.save(EmployeeCreator.createEmployeeToBeSaved());
        employeeUserRepository.save(USER);

        String expectedName = savedEmployee.getName();
        String expectedOccupation = savedEmployee.getOccupation();
        Integer expectedAge = savedEmployee.getAge();
        BigDecimal expectedSalary = savedEmployee.getSalary();

        log.info("expectedSalary {} " + expectedSalary);

        List<Employee> employees = testResTemplateRoleUser.exchange("/employees/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Employee>>() {
                }).getBody();

        log.info("salary employees {} " + employees.get(0).getSalary());

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
    @DisplayName("findByName Returns Employee When Successful")
    void findByName_ReturnsEmployee_WhenSuccessful() {
        Employee savedEmployee = this.employeeRepository.save(EmployeeCreator.createEmployeeToBeSaved());
        employeeUserRepository.save(USER);

        String expectedName = savedEmployee.getName();
        String url = String.format("/employees/find?name=%s", expectedName);

        List<Employee> employees = testResTemplateRoleUser.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Employee>>() {
                }).getBody();

        Assertions.assertThat(employees)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(employees.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName Returns List Of Empty When Employee Is Not Found")
    void findByName_ReturnsListOfEmpty_WhenEmployeeIsNotFound() {
        employeeUserRepository.save(USER);
        List<Employee> employees = testResTemplateRoleUser.exchange("/employees/find?name=test", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Employee>>() {
                }).getBody();

        Assertions.assertThat(employees)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById Returns Employee When Successful")
    void findById_ReturnsEmployee_WhenSuccessful() {
        Employee savedEmployee = this.employeeRepository.save(EmployeeCreator.createEmployeeToBeSaved());
        employeeUserRepository.save(USER);

        Long expectedId = savedEmployee.getId();

        Employee employee = testResTemplateRoleUser.getForObject("/employees/{id}", Employee.class, expectedId);

        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("save Returns Employee When Successful")
    void save_ReturnsEmployee_WhenSuccessful() {
        EmployeePostRequestBody employeePostRequestBody = EmployeePostRequestBodyCreator.createEmployeePostRequestBody();
        employeeUserRepository.save(USER);

        ResponseEntity<Employee> employeeResponseEntity = testResTemplateRoleUser.postForEntity("/employees", employeePostRequestBody, Employee.class);

        Assertions.assertThat(employeeResponseEntity).isNotNull();
        Assertions.assertThat(employeeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(employeeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(employeeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace Updates Employee When Successful")
    void replace_UpdatesEmployee_WhenSuccessful() {
        Employee savedEmployee = this.employeeRepository.save(EmployeeCreator.createEmployeeToBeSaved());
        employeeUserRepository.save(USER);

        savedEmployee.setName("test");
        savedEmployee.setOccupation("test");
        savedEmployee.setAge(0);
        savedEmployee.setSalary(BigDecimal.valueOf(0.00).setScale(2));

        ResponseEntity<Void> employeeResponseEntity = testResTemplateRoleUser.exchange("/employees",
                HttpMethod.PUT, new HttpEntity<>(savedEmployee), Void.class);

        Assertions.assertThat(employeeResponseEntity).isNotNull();
        Assertions.assertThat(employeeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete Removes Employee When Successful")
    void delete_RemovesEmployee_WhenSuccessful() {
        Employee savedEmployee = this.employeeRepository.save(EmployeeCreator.createEmployeeToBeSaved());
        employeeUserRepository.save(ADMIN);

        ResponseEntity<Void> employeeResponseEntity = testResTemplateRoleAdmin.exchange("/employees/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedEmployee.getId());

        Assertions.assertThat(employeeResponseEntity).isNotNull();
        Assertions.assertThat(employeeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete Returns 403 When User Is Not Admin")
    void delete_Returns403_WhenUserIsNotAdmin() {
        Employee savedEmployee = this.employeeRepository.save(EmployeeCreator.createEmployeeToBeSaved());
        employeeUserRepository.save(USER);

        ResponseEntity<Void> employeeResponseEntity = testResTemplateRoleUser.exchange("/employees/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedEmployee.getId());

        Assertions.assertThat(employeeResponseEntity).isNotNull();
        Assertions.assertThat(employeeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
