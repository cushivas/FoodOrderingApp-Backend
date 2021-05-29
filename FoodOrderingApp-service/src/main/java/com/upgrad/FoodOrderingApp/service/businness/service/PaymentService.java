package com.upgrad.FoodOrderingApp.service.businness.service;

import java.util.List;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;

public interface PaymentService {

	public List<PaymentEntity> getAllPaymentMethods();

	public PaymentEntity getPaymentMethod(String paymentUuid) throws PaymentMethodNotFoundException;

}
