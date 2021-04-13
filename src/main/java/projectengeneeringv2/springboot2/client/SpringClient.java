package projectengeneeringv2.springboot2.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import projectengeneeringv2.springboot2.model.Employee;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {

    public static void main(String[] args) {

        ResponseEntity<Employee> entity = new RestTemplate()
                .getForEntity("http://localhost:8080/employees/{id}", Employee.class, 2);

        log.info(entity);

        Employee object = new RestTemplate().getForObject("http://localhost:8080/employees/{id}",
                Employee.class, 2);

        log.info(object);

        Employee[] employees = new RestTemplate().getForObject("http://localhost:8080/employees/all", Employee[].class);

        log.info(employees);

        log.info(Arrays.toString(employees));

        ResponseEntity<List<Employee>> exchange = new RestTemplate().exchange("http://localhost:8080/employees/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        log.info(exchange.getBody());

//        Employee expectedEmployee = Employee.builder()
//                .name("Isabela")
//                .occupation("Administradora")
//                .age(27)
//                .salary(BigDecimal.valueOf(2300.00))
//                .build();
//
//        Employee employeeSaved = new RestTemplate().postForObject("http://localhost:8080/employees/", expectedEmployee, Employee.class);
//
//        log.info("saved employee {}", employeeSaved);

        Employee expectedEmployee = Employee.builder()
                .name("Débora")
                .occupation("Designer")
                .age(28)
                .salary(BigDecimal.valueOf(2400.00))
                .build();

        ResponseEntity<Employee> employeeSaved = new RestTemplate().exchange("http://localhost:8080/employees/",
                HttpMethod.POST,
                new HttpEntity<>(expectedEmployee),
                Employee.class);

        log.info("saved employee {}", employeeSaved, createJsonHeader());

        Employee employeeToBeUpdated = employeeSaved.getBody();
        employeeToBeUpdated.setName("Marlene");

        new RestTemplate().exchange("http://localhost:8080/employees/",
                HttpMethod.PUT,
                new HttpEntity<>(employeeToBeUpdated, createJsonHeader()),
                Void.class);

        log.info(employeeToBeUpdated);

        ResponseEntity<Void> employeeDeleted = new RestTemplate().exchange("http://localhost:8080/employees/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                employeeToBeUpdated.getId());

        log.info(employeeDeleted);

    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
