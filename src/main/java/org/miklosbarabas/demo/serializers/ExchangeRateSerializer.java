package org.miklosbarabas.demo.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class ExchangeRateSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeString(value.setScale(5, BigDecimal.ROUND_HALF_UP).toString());
    }
}

