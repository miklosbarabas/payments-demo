package org.miklosbarabas.demo.projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.miklosbarabas.demo.models.Payment;
import org.miklosbarabas.demo.models.PaymentAttributes;
import org.springframework.data.rest.core.config.Projection;

/**
 * Projection for {@link Payment} & {@link PaymentAttributes}
 *
 * @author Miklos Barabas
 */
@Projection(name = "paymentAttributesProjection", types = { Payment.class })
public interface PaymentAttributesProjection {
    String getType();
    String getId();
    String getVersion();
    @JsonProperty("attributes")
    PaymentAttributes getPaymentAttributes();

}
