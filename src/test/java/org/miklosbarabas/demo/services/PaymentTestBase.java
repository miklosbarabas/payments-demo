package org.miklosbarabas.demo.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.miklosbarabas.demo.Application;
import org.miklosbarabas.demo.models.Payment;
import org.miklosbarabas.demo.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public abstract class PaymentTestBase {
    @LocalServerPort
    protected int PORT;
    protected static String HOST = "http://localhost";
    protected static String PAYMENTS_PATH = "/api/payments/";
    static int NUMBER_OF_TESTPAYMENTS_TO_DB = 3;
    ArrayList<Payment> testPayments;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PaymentRepository paymentRepository;


    @Before
    public void setup() throws IOException {
        // Load test data from JSON
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/testdata/payments.json")) {
            TypeReference<List<Payment>> typeReference = new TypeReference<List<Payment>>() {};
            testPayments = objectMapper.readValue(inputStream, typeReference);
        }

        // Save only the first NUMBER_OF_TESTPAYMENTS_TO_DB test entity to DB
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

}
