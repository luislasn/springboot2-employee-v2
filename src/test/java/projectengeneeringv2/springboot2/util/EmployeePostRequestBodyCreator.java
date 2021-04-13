package projectengeneeringv2.springboot2.util;

import projectengeneeringv2.springboot2.requests.EmployeePostRequestBody;

public class EmployeePostRequestBodyCreator {

    public static EmployeePostRequestBody createEmployeePostRequestBody() {
        return EmployeePostRequestBody.builder()
                .name(EmployeeCreator.createEmployeeToBeSaved().getName())
                .occupation(EmployeeCreator.createEmployeeToBeSaved().getOccupation())
                .age(EmployeeCreator.createEmployeeToBeSaved().getAge())
                .salary(EmployeeCreator.createEmployeeToBeSaved().getSalary())
                .build();
    }

}
