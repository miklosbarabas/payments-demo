package org.miklosbarabas.demo.events;

import org.miklosbarabas.demo.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class PaymentCascadeSaveMongoEventListener extends AbstractMongoEventListener<Object> {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        if ((source instanceof Payment) && (((Payment) source).getPaymentAttributes() != null)) {
            mongoOperations.save(((Payment) source).getPaymentAttributes());
        }
    }
}