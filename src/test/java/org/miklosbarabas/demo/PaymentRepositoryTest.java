package org.miklosbarabas.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.miklosbarabas.demo.models.Payment;
import org.miklosbarabas.demo.repositories.PaymentAttributesRepository;
import org.miklosbarabas.demo.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Integration tests for {@link PaymentRepository} & {@link PaymentAttributesRepository}
 *
 * @author Miklos Barabas
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentAttributesRepository paymentAttributesRepository;

    @Autowired
    ObjectMapper objectMapper;

    private ArrayList<Payment> testPayments;

    @Before
    public void initDb() throws IOException {
        clearDb();

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/testdata/payments.json")) {
            TypeReference<List<Payment>> typeReference = new TypeReference<List<Payment>>(){};
            testPayments = objectMapper.readValue(inputStream, typeReference);
        }
        paymentRepository.save(testPayments);
    }

    @After
    public void clearDb() {
        paymentRepository.deleteAll();
        paymentAttributesRepository.deleteAll();
    }

    @Test
    public void findAllPaymentsTest() {
        ArrayList<Payment> allPayments = Lists.newArrayList(paymentRepository.findAll());
        assertEquals(allPayments.size(), testPayments.size());
        assertEquals(allPayments, testPayments);
    }

    @Test
    public void findPaymentByIdTest() {
        ArrayList<Payment> payments = Lists.newArrayList(paymentRepository.findAll());
        Payment payment = payments.get(0);
        Payment paymentById = paymentRepository.findOne(payment.getId());
        assertEquals(payment, paymentById);
    }

    @Test
    public void savePaymentTest() {
        long count = paymentRepository.count();
        Payment paymentToSave = testPayments.get(0);
        paymentToSave.setId("newId");
        paymentToSave.setVersion(null);
        paymentRepository.save(paymentToSave);
        assertEquals(paymentRepository.count(),count+1);
    }

    @Test
    public void updatePaymentTest() {
        Payment testPayment = testPayments.get(0);
        Payment paymentToUpdate = paymentRepository.findOne(testPayment.getId());
        assertEquals(testPayment, paymentToUpdate);

        paymentToUpdate.setType("TESTPAYMENT");
        paymentRepository.save(paymentToUpdate);

        Payment paymentUpdated = paymentRepository.findOne(testPayment.getId());

        assertNotEquals(testPayment, paymentToUpdate);
        assertNotEquals(testPayment.getType(), paymentToUpdate.getType());
        assertEquals(testPayment.getVersion() + 1, (long) paymentToUpdate.getVersion());
        assertEquals(testPayment.getId(), paymentToUpdate.getId());
        assertEquals(testPayment.getOrganisationId(), paymentToUpdate.getOrganisationId());
        assertEquals(testPayment.getPaymentAttributes(), paymentToUpdate.getPaymentAttributes());

        assertEquals(paymentUpdated, paymentToUpdate);
    }

    @Test
    public void deletePaymentTest() {
        long count = paymentRepository.count();

        String paymentIdToDelete = testPayments.get(0).getId();

        Payment paymentToDelete = paymentRepository.findOne(paymentIdToDelete);
        assertEquals(testPayments.get(0), paymentToDelete);

        paymentRepository.delete(paymentIdToDelete);

        Payment paymentDeleted = paymentRepository.findOne(paymentIdToDelete);
        assertNotEquals(testPayments.get(0), paymentDeleted);
        assertNull(paymentDeleted);

        long newCount = paymentRepository.count();
        assertEquals(count - 1, newCount);
    }

}