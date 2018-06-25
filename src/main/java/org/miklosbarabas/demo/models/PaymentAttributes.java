package org.miklosbarabas.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.miklosbarabas.demo.serializers.AmountSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "paymentattributes")
@TypeAlias("PaymentAttributes")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PaymentAttributes {

    @Id
    private String id;

    @JsonSerialize(using = AmountSerializer.class)
    @NotNull
    private BigDecimal amount;

    @Valid
    private Account beneficiaryParty;

    @Valid
    private ChargesInformation chargesInformation;

    private Currency currency;

    @Valid
    private Account debtorParty;

    private String endToEndReference;

    private FX fx;

    private String numericReference;

    private String paymentId;

    private String paymentPurpose;

    private String paymentScheme;

    private String paymentType;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate processingDate;

    @NotEmpty
    private String reference;

    private String schemePaymentSubType;

    @NotEmpty
    private String schemePaymentType;

    @Valid
    private Party sponsorParty;
}
