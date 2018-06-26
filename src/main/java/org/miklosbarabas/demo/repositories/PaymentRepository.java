package org.miklosbarabas.demo.repositories;

import io.swagger.annotations.Api;
import org.miklosbarabas.demo.models.Payment;
import org.miklosbarabas.demo.projections.PaymentAttributesProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * REST repository resource for {@link Payment}
 *
 * @author Miklos Barabas
 */
@Api(tags = { "Payments API" }, description = "Endpoints to fetch, create, update, delete Payment resources")
@RepositoryRestResource(
        path = "payments",
        collectionResourceRel = "data",
        excerptProjection = PaymentAttributesProjection.class
)
public interface PaymentRepository extends CrudRepository<Payment, String> {

}
