package org.miklosbarabas.demo.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
class Party {
    @NotEmpty
    private String accountNumber;

    private String bankId;

    private String bankIdCode;
}
