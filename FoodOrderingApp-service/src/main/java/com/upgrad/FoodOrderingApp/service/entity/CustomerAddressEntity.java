package com.upgrad.FoodOrderingApp.service.entity;

/**
 * @author swapnadeep.dutta
 * 
 * This class is for fetching and storing the information regarding the coupons
 */

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "customer_adress")
@NamedQueries({
		@NamedQuery(name = "customerAddressByAddressId", query = "select cad from CustomerAddressEntity cad where cad.address = :address"),
		@NamedQuery(name = "customerAddressesByCustomerId", query = "select cad from CustomerAddressEntity cad where cad.customer = :customer") })
public class CustomerAddressEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "address_id")
	private AddressEntity addressEntity;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "customer_id")
	private CustomerEntity customerEntity;

	// No-argument Constructor for empty initializing

	public CustomerAddressEntity() {

	}

	// Initialized constructor

	public CustomerAddressEntity(long id, AddressEntity addressEntity, CustomerEntity customerEntity) {
		this.id = id;
		this.addressEntity = addressEntity;
		this.customerEntity = customerEntity;
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

	public AddressEntity getAddressEntity() {
		return addressEntity;
	}

	public void setAddressEntity(AddressEntity addressEntity) {
		this.addressEntity = addressEntity;
	}

	public CustomerEntity getCustomerEntity() {
		return customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

}
