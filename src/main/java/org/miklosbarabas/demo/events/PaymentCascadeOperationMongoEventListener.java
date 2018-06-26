package org.miklosbarabas.demo.events;

import com.mongodb.DBObject;
import org.miklosbarabas.demo.models.Payment;
import org.miklosbarabas.demo.models.PaymentAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

/**
 * EventListener to handle cascading operations
 *
 * @author Miklos Barabas
 */
@Component
public class PaymentCascadeOperationMongoEventListener extends AbstractMongoEventListener<Object> {
    private final MongoOperations mongoOperations;

    @Autowired
    public PaymentCascadeOperationMongoEventListener(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        if (source instanceof Payment) {
            mongoOperations.save(((Payment) source).getPaymentAttributes());
        }
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        DBObject source = event.getSource();
        String paymentId = source.get("_id").toString();
        Payment payment = mongoOperations.findById(paymentId, Payment.class);
        if (payment != null) {
            PaymentAttributes paymentAttributes = payment.getPaymentAttributes();
            if (paymentAttributes != null) {
                mongoOperations.remove(paymentAttributes);
            }
        }
    }
}