package org.miklosbarabas.demo.services;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.miklosbarabas.demo.models.Payment;
import org.miklosbarabas.demo.repositories.PaymentAttributesRepository;
import org.miklosbarabas.demo.repositories.PaymentRepository;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Integration tests for {@link PaymentRepository} & {@link PaymentAttributesRepository}
 *
 * @author Miklos Barabas
 */
@DirtiesContext
public class PaymentRepositoryTest extends PaymentTestBase {
    @BeforeClass
    public static void init() {
        NUMBER_OF_TESTPAYMENTS_TO_DB = 14;
    }


    @Test
    public void findAllPaymentsTest() {
        ArrayList<Payment> allPayments = Lists.newArrayList(paymentRepository.findAll());
        assertEquals(allPayments.size(), NUMBER_OF_TESTPAYMENTS_TO_DB);
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