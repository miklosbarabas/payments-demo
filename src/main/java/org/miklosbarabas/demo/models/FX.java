package org.miklosbarabas.demo.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.miklosbarabas.demo.serializers.AmountSerializer;
import org.miklosbarabas.demo.serializers.ExchangeRateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
class FX {
    private String contractReference;

    @JsonSerialize(using = ExchangeRateSerializer.class)
    private BigDecimal exchangeRate;

    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal originalAmount;

    private Currency originalCurrency;


}
