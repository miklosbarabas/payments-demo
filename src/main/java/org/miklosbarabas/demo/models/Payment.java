package org.miklosbarabas.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Document(collection = "payments")
@TypeAlias("Payment")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Payment {
    @NotEmpty
    private String type;

    @Id
    private String id;

    @Version
    private Long version;

    @NotNull
    private String organisationId;

    @DBRef
    @Field("attributes")
    @JsonProperty("attributes")
    @NotNull
    private PaymentAttributes paymentAttributes;


    public Payment(String type, String organisationId, PaymentAttributes paymentAttributes) {
        this.type = type;
        this.organisationId = organisationId;
        this.paymentAttributes = paymentAttributes;
    }
}
