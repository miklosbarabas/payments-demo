package org.miklosbarabas.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.miklosbarabas.demo.models.Payment;
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
    ObjectMapper objectMapper;

    private ArrayList<Payment> testPayments;

    @Before
    public void setup() throws IOException {
        // Load test data from JSON
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/testdata/payments.json")) {
            TypeReference<List<Payment>> typeReference = new TypeReference<List<Payment>>() {
            };
            testPayments = objectMapper.readValue(inputStream, typeReference);
        }

        // Save only the first test entity to DB
        paymentRepository.save(testPayments.get(0));

        // Make Jackson not to fail on parsing the DTO because the response contains the _links attribute
        // because of the HATEAOS approach, but the DTO itself does not have that field.
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (type, s) -> {
                    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    return objectMapper;
                }
        ));
    }

    @After
    public void clearDb() {
        paymentRepository.deleteAll();
    }


    @Test
    public void whenGetPayment_thenOKandPaymentsEquals() {
        Payment testPayment = testPayments.get(0);

        Payment paymentReturned = given()
                .when().get(PAYMENTS_ENDPOINT + testPayment.getId())
                .then().statusCode(200).and().extract().as(Payment.class);

        // Copying missing fields for paymentReturned as those are returned in the response
        paymentReturned.setId(testPayment.getId());
        paymentReturned.getPaymentAttributes().setId(testPayment.getPaymentAttributes().getId());
        paymentReturned.setVersion(testPayment.getVersion());

        assertEquals(testPayment, paymentReturned);
    }

    @Test
    public void whenSavePayment_thenOKandPaymentsEquals() {
        Payment testPayment = testPayments.get(5);

        Payment paymentReturned = given().contentType("application/json").body(testPayment)
                .when().post(PAYMENTS_ENDPOINT)
                .then().extract().as(Payment.class);

        // Copying missing fields for paymentReturned as those are returned in the response
        paymentReturned.setId(testPayment.getId());
        paymentReturned.getPaymentAttributes().setId(testPayment.getPaymentAttributes().getId());
        paymentReturned.setVersion(testPayment.getVersion());

        assertEquals(testPayment, paymentReturned);
    }

}

