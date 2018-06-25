package org.miklosbarabas.demo.repositories;

import org.miklosbarabas.demo.models.Payment;
import org.miklosbarabas.demo.projections.PaymentAttributesProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        path = "payments",
        collectionResourceRel = "data",
        excerptProjection = PaymentAttributesProjection.class
)
public interface PaymentRepository extends CrudRepository<Payment, String> {

}
