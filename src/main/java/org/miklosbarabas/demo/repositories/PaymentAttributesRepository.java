package org.miklosbarabas.demo.repositories;

import org.miklosbarabas.demo.models.PaymentAttributes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * REST repository resource for {@link PaymentAttributes}
 *
 * @author Miklos Barabas
 */
@RepositoryRestResource(
        path = "paymentattributes",
        collectionResourceRel = "attributes",
        exported = false
)
public interface PaymentAttributesRepository extends CrudRepository<PaymentAttributes, String> {

}
