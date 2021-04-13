package projectengeneeringv2.springboot2.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePostRequestBody {

    @NotEmpty(message = "The employee name cannot be empty")
    private String name;

    @NotEmpty(message = "The employee occupation cannot be empty")
    private String occupation;

    @NotNull(message = "The employee age cannot be null")
    private Integer age;

    @NotNull(message = "The employee salary cannot be null")
    private BigDecimal salary;

}
