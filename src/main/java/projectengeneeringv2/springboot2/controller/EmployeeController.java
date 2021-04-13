package projectengeneeringv2.springboot2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import projectengeneeringv2.springboot2.model.Employee;
import projectengeneeringv2.springboot2.requests.EmployeePostRequestBody;
import projectengeneeringv2.springboot2.requests.EmployeePutRequestBody;
import projectengeneeringv2.springboot2.service.EmployeeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Log4j2
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "List all employees paginated",
               description = "The default size is 20, use the parameter size to change the default value",
               tags = {"employee"})
    public ResponseEntity<Page<Employee>> listAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(employeeService.listAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> listAllNonPageable() {
        return ResponseEntity.ok(employeeService.listAllNonPageable());
    }

    @GetMapping("/find")
    public ResponseEntity<List<Employee>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.findByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping("by-id/{id}")
    public ResponseEntity<Employee> findByIdAuthenticationPrincipal(@PathVariable Long id,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails);
        return ResponseEntity.ok(employeeService.findByIdOrThrowBadRequestException(id));
    }

    @PostMapping
    public ResponseEntity<Employee> save(@RequestBody @Valid EmployeePostRequestBody employeePostRequestBody) {
        return new ResponseEntity<>(employeeService.save(employeePostRequestBody), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody EmployeePutRequestBody employeePutRequestBody) {
        employeeService.replace(employeePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/admin/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When employee does not exists in the database")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
