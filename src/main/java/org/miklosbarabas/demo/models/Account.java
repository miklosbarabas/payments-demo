package org.miklosbarabas.demo.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
class Account extends Party {
    @NotEmpty
    private String accountName;

    @NotEmpty
    private String accountNumberCode;

    private Long accountType;

    private String address;

    private String name;

}
