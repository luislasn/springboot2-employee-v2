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
public class EmployeePutRequestBody {

    private Long id;
    private String name;
    private String occupation;
    private Integer age;
    private BigDecimal salary;

}
