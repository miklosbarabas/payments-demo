package org.miklosbarabas.demo.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.miklosbarabas.demo.serializers.AmountSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
class Charge {
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal amount;

    private Currency currency;
}
