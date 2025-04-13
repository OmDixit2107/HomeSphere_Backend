package com.homesphere_backend.service;

import com.homesphere_backend.entity.Payment;

import java.util.List;

public interface PaymentService {
    Payment createPayment(Payment payment);
    List<Payment> getPaymentsByUserId(Long userId);
    List<Payment> getPaymentsByPropertyId(Long propertyId);
}
