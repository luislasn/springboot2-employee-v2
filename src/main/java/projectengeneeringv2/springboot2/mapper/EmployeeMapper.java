package projectengeneeringv2.springboot2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import projectengeneeringv2.springboot2.model.Employee;
import projectengeneeringv2.springboot2.requests.EmployeePostRequestBody;
import projectengeneeringv2.springboot2.requests.EmployeePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {

    public static final EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    public abstract Employee toEmployee(EmployeePostRequestBody employeePostRequestBody);

    public abstract Employee toEmployee(EmployeePutRequestBody employeePutRequestBody);

}
