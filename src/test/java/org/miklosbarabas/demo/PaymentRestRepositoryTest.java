package org.miklosbarabas.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.miklosbarabas.demo.models.Payment;
import org.miklosbarabas.demo.repositories.PaymentAttributesRepository;
import org.miklosbarabas.demo.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)

public class PaymentRestRepositoryTest {
    private static final String PAYMENTS_ENDPOINT = "http://localhost:8080/api/payments/";


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentAttributesRepository paymentAttributesRepository;

    @Autowired
    ObjectMapper objectMapper;

    private ArrayList<Payment> testPayments;

    @Before
    public void setup() throws IOException {
        // Load test data from JSON and save to DB
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/testdata/payments.json")) {
            TypeReference<List<Payment>> typeReference = new TypeReference<List<Payment>>() {
            };
            testPayments = objectMapper.readValue(inputStream, typeReference);
        }
        paymentRepository.save(testPayments);

        // Make Jackson not to fail on parsing the DTO because the response contains the _links attribute
        // because of the HATEAOS approach, but the DTO itself does not have that field.
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (type, s) -> {
                    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    return objectMapper;
                }
        ));
    }


    @Test
    public void whenGetPayment_thenOKandPaymentsEquals() {
        Payment payment = testPayments.get(0);

        Payment paymentReturned = given()
                .when().get(PAYMENTS_ENDPOINT + payment.getId())
                .then().statusCode(200).and().extract().as(Payment.class);

        //Setting up the IDs for paymentReturned because the IDs are in the context path, and not returned in the response
        paymentReturned.setId(payment.getId());
        paymentReturned.getPaymentAttributes().setId(payment.getPaymentAttributes().getId());
        paymentReturned.setVersion(payment.getVersion());

        assertEquals(payment, paymentReturned);
    }
}

