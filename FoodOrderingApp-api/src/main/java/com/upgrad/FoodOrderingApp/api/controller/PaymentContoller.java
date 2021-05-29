package com.upgrad.FoodOrderingApp.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.service.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;

@RestController
@CrossOrigin
public class PaymentContoller {

	@Autowired
	private PaymentService paymentService;

	@RequestMapping()
	public ResponseEntity<PaymentListResponse> getPaymentResponse() {
		List<PaymentEntity> paymentEntities = paymentService.getAllPaymentMethods();
		PaymentListResponse paymentListResponse = new PaymentListResponse();
		for (PaymentEntity paymentEntity : paymentEntities) {
			PaymentResponse paymentResponse = new PaymentResponse();
			paymentResponse.setId(UUID.fromString(paymentEntity.getUuid()));
			paymentResponse.setPaymentName(paymentEntity.getPaymentName());
			paymentListResponse.addPaymentMethodsItem(paymentResponse);
		}
		return new ResponseEntity<PaymentListResponse>(paymentListResponse, HttpStatus.OK);
	}

}
