package com.upgrad.FoodOrderingApp.service.businness.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.FoodOrderingApp.service.businness.service.PaymentService;
import com.upgrad.FoodOrderingApp.service.dao.impl.PaymentDaoImpl;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentDaoImpl paymentDaoImpl;

	@Override
	public List<PaymentEntity> getAllPaymentMethods() {
		return paymentDaoImpl.getAllPaymentMethods();
	}

	@Override
	public PaymentEntity getPaymentMethod(String paymentUuid) throws PaymentMethodNotFoundException {
		PaymentEntity paymentEntity = paymentDaoImpl.getPaymentMethodById(paymentUuid);
		if (paymentEntity == null) {
			throw new PaymentMethodNotFoundException("PNF-002", "No Payment Method found for this id");
		} else {
			return paymentEntity;
		}
	}

}
