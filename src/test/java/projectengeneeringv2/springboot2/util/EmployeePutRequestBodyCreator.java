package projectengeneeringv2.springboot2.util;

import projectengeneeringv2.springboot2.requests.EmployeePostRequestBody;
import projectengeneeringv2.springboot2.requests.EmployeePutRequestBody;

public class EmployeePutRequestBodyCreator {

    public static EmployeePutRequestBody createEmployeePutRequestBody() {
        return EmployeePutRequestBody.builder()
                .id(EmployeeCreator.createValidUpdatedEmployee().getId())
                .name(EmployeeCreator.createValidUpdatedEmployee().getName())
                .occupation(EmployeeCreator.createValidUpdatedEmployee().getOccupation())
                .age(EmployeeCreator.createValidUpdatedEmployee().getAge())
                .salary(EmployeeCreator.createValidUpdatedEmployee().getSalary())
                .build();
    }

}
