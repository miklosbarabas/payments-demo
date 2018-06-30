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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class PaymentRestRepositoryTest {
    private static final String PAYMENTS_ENDPOINT = "http://localhost:8080/api/payments/";
    private static final int NUMBER_OF_TESTPAYMENTS_TO_DB = 3;
    private ArrayList<Payment> testPayments;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    ObjectMapper objectMapper;


    @Before
    public void setup() throws IOException {
        // Load test data from JSON
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/testdata/payments.json")) {
            TypeReference<List<Payment>> typeReference = new TypeReference<List<Payment>>() {
            };
            testPayments = objectMapper.readValue(inputStream, typeReference);
        }

        // Save only the first testPaymentsToDatabase number of test entity to DB
        for (int i = 0; i < NUMBER_OF_TESTPAYMENTS_TO_DB; i++) {
            paymentRepository.save(testPayments.get(i));
        }

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

        Payment paymentReturned = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PAYMENTS_ENDPOINT + testPayment.getId())
                .then().statusCode(200).and().extract().as(Payment.class);

        // Copying missing fields for paymentReturned as those are not returned in the response
        paymentReturned.setId(testPayment.getId());
        paymentReturned.getPaymentAttributes().setId(testPayment.getPaymentAttributes().getId());
        paymentReturned.setVersion(testPayment.getVersion());

        assertEquals(testPayment, paymentReturned);
    }

    @Test
    public void whenGetAllPayments_thenOKandPaymentsEquals() {
        ArrayList<HashMap> paymentsReturned = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PAYMENTS_ENDPOINT)
                .then().statusCode(200).and().extract().path("_embedded.data");

        ArrayList<Payment> matchingPaymentsByIds = new ArrayList<>();
        for (int i = 0; i < paymentsReturned.size(); i++) {
            Payment paymentReturned = objectMapper.convertValue(paymentsReturned.get(i), Payment.class);
            Optional<Payment> matchingPaymentByIds = testPayments.stream().
                    filter(p -> p.getId().equals(paymentReturned.getId())).
                    findFirst();

            assertTrue(matchingPaymentByIds.isPresent());

            // set ID because the projection does not contain the PaymentAttributes ID
            paymentReturned.getPaymentAttributes().setId(matchingPaymentByIds.get().getPaymentAttributes().getId());
            assertEquals(paymentReturned, matchingPaymentByIds.get());

            matchingPaymentsByIds.add(matchingPaymentByIds.get());
        }

        assertEquals(NUMBER_OF_TESTPAYMENTS_TO_DB, matchingPaymentsByIds.size());
    }

    @Test
    public void whenSavePayment_thenOKandPaymentsEquals() {
        Payment testPayment = testPayments.get(NUMBER_OF_TESTPAYMENTS_TO_DB + 1);

        Payment paymentReturned = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(testPayment)
                .when().post(PAYMENTS_ENDPOINT)
                .then().statusCode(201).extract().as(Payment.class);

        // Copying missing fields for paymentReturned as those are not returned in the response
        paymentReturned.setId(testPayment.getId());
        paymentReturned.getPaymentAttributes().setId(testPayment.getPaymentAttributes().getId());
        paymentReturned.setVersion(testPayment.getVersion());

        assertEquals(testPayment, paymentReturned);
    }

    @Test
    public void whenDeletePayment_thenOKandGetPaymentNotFound() {
        Payment testPayment = testPayments.get(0);

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(PAYMENTS_ENDPOINT + testPayment.getId())
                .then().statusCode(204);

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PAYMENTS_ENDPOINT + testPayment.getId())
                .then().statusCode(404);
    }

    @Test
    public void whenUpdatePayment_thenOKandGetPaymentEquals() {
        Payment testPayment = testPayments.get(0);
        testPayment.getPaymentAttributes().setNumericReference("TESTNUMREF");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(testPayment)
                .when().patch(PAYMENTS_ENDPOINT + testPayment.getId())
                .then().statusCode(200);

        Payment paymentUpdated = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PAYMENTS_ENDPOINT + testPayment.getId())
                .then().statusCode(200).extract().as(Payment.class);

        // Copying missing fields for paymentUpdated as those are not returned in the response
        paymentUpdated.setId(testPayment.getId());
        paymentUpdated.getPaymentAttributes().setId(testPayment.getPaymentAttributes().getId());
        paymentUpdated.setVersion(testPayment.getVersion());

        assertEquals(testPayment, paymentUpdated);
    }

}

