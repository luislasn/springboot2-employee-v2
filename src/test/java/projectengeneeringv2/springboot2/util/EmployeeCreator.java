package projectengeneeringv2.springboot2.util;

import projectengeneeringv2.springboot2.model.Employee;

import java.math.BigDecimal;

public class EmployeeCreator {

    public static Employee createEmployeeToBeSaved() {
        return Employee.builder()
                .name("Artemis Castro")
                .occupation("Marketing")
                .age(22)
                .salary(BigDecimal.valueOf(2750.00).setScale(2))
                .build();
    }

    public static Employee createValidEmployee() {
        return Employee.builder()
                .name("Artemis Castro")
                .occupation("Marketing")
                .age(22)
                .salary(BigDecimal.valueOf(2750.00).setScale(2))
                .id(1L)
                .build();
    }

    public static Employee createValidUpdatedEmployee() {
        return Employee.builder()
                .name("Artemis Castro 2")
                .occupation("Marketing")
                .age(22)
                .salary(BigDecimal.valueOf(2750.00).setScale(2))
                .id(1L)
                .build();
    }

}
