package org.miklosbarabas.demo.services;

import org.junit.BeforeClass;
import org.junit.Test;
import org.miklosbarabas.demo.models.Payment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@DirtiesContext
public class PaymentRestRepositoryTest extends PaymentTestBase {
    @BeforeClass
    public static void init() {
        NUMBER_OF_TESTPAYMENTS_TO_DB = 3;
    }


    @Test
    public void whenGetPayment_thenOKandPaymentsEquals() {
        Payment testPayment = testPayments.get(0);
        String testPaymentId = testPayment.getId();

        Payment paymentReturned =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).baseUri(HOST).port(PORT).basePath(PAYMENTS_PATH + testPaymentId)
                .when().get()
                .then().statusCode(200).and().extract().as(Payment.class);

        // Copying missing fields for paymentReturned as those are not returned in the response
        paymentReturned.setId(testPayment.getId());
        paymentReturned.getPaymentAttributes().setId(testPayment.getPaymentAttributes().getId());
        paymentReturned.setVersion(testPayment.getVersion());

        assertEquals(testPayment, paymentReturned);
    }

    @Test
    public void whenGetAllPayments_thenOKandPaymentsEquals() {
        ArrayList<HashMap> paymentsReturned =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).baseUri(HOST).port(PORT).basePath(PAYMENTS_PATH)
                .when().get()
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

        Payment paymentReturned =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).baseUri(HOST).port(PORT).basePath(PAYMENTS_PATH).body(testPayment)
                .when().post()
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
        String testPaymentId = testPayment.getId();

        given().contentType(MediaType.APPLICATION_JSON_VALUE).baseUri(HOST).port(PORT).basePath(PAYMENTS_PATH + testPaymentId)
                .when().delete()
                .then().statusCode(204);

        given().contentType(MediaType.APPLICATION_JSON_VALUE).baseUri(HOST).port(PORT).basePath(PAYMENTS_PATH + testPaymentId)
                .when().get()
                .then().statusCode(404);
    }

    @Test
    public void whenUpdatePayment_thenOKandGetPaymentEquals() {
        Payment testPayment = testPayments.get(0);
        String testPaymentId = testPayment.getId();
        testPayment.getPaymentAttributes().setNumericReference("TESTNUMREF");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(testPayment).baseUri(HOST).port(PORT).basePath(PAYMENTS_PATH + testPaymentId)
                .when().patch()
                .then().statusCode(200);

        Payment paymentUpdated =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).baseUri(HOST).port(PORT).basePath(PAYMENTS_PATH + testPaymentId)
                .when().get()
                .then().statusCode(200).extract().as(Payment.class);

        // Copying missing fields for paymentUpdated as those are not returned in the response
        paymentUpdated.setId(testPayment.getId());
        paymentUpdated.getPaymentAttributes().setId(testPayment.getPaymentAttributes().getId());
        paymentUpdated.setVersion(testPayment.getVersion());

        assertEquals(testPayment, paymentUpdated);
    }

}

