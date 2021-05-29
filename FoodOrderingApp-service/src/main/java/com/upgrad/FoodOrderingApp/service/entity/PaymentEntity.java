package com.upgrad.FoodOrderingApp.service.entity;

/**
 * @author saranshgupta1995
 * 
 * This class is for fetching and storing the information regarding the payment
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "payment")
@NamedQueries({ @NamedQuery(name = "allPaymentMethods", query = "select p from PaymentEntity p"),
		@NamedQuery(name = "getMethodById", query = "select p fro PaymentEntity p where p.uuid = :uuid") })
public class PaymentEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "uuid")
	@Size(max = 255)
	@NotNull
	private String uuid;

	@Column(name = "payment_name")
	@Size(max = 255)
	@NotNull
	private String paymentName;

	// No-argument Constructor for empty initializing

	public PaymentEntity() {

	}

	// Initialized constructor

	public PaymentEntity(long id, @Size(max = 255) @NotNull String uuid, @Size(max = 255) @NotNull String paymentName) {
		this.id = id;
		this.uuid = uuid;
		this.paymentName = paymentName;
	}

	/*
	 * Getters and Setters method to fetch and store the information in the
	 * databases
	 */

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

}
