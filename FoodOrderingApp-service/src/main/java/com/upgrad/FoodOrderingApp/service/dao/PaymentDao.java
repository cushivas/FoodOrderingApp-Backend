package com.upgrad.FoodOrderingApp.service.dao;

import java.util.List;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;

public interface PaymentDao {

	public List<PaymentEntity> getAllPaymentMethods();

	public PaymentEntity getPaymentMethodById(final String uuid);

}
