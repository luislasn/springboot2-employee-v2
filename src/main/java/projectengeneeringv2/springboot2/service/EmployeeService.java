package projectengeneeringv2.springboot2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectengeneeringv2.springboot2.exception.BadRequestException;
import projectengeneeringv2.springboot2.mapper.EmployeeMapper;
import projectengeneeringv2.springboot2.model.Employee;
import projectengeneeringv2.springboot2.repository.EmployeeRepository;
import projectengeneeringv2.springboot2.requests.EmployeePostRequestBody;
import projectengeneeringv2.springboot2.requests.EmployeePutRequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Page<Employee> listAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public List<Employee> listAllNonPageable() {
        return employeeRepository.findAll();
    }

    public List<Employee> findByName(String name) {
        return employeeRepository.findByName(name);

    }

    public Employee findByIdOrThrowBadRequestException(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Employee not found"));
    }

    @Transactional
    public Employee save(EmployeePostRequestBody employeePostRequestBody) {
        return employeeRepository.save(EmployeeMapper.INSTANCE.toEmployee(employeePostRequestBody));
    }

    public void replace(EmployeePutRequestBody employeePutRequestBody) {
        Employee savedEmployee = findByIdOrThrowBadRequestException(employeePutRequestBody.getId());
        Employee employee = EmployeeMapper.INSTANCE.toEmployee(employeePutRequestBody);
        employee.setId(savedEmployee.getId());
        employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

}
