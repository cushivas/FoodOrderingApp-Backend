package com.upgrad.FoodOrderingApp.service.entity;

/**
 * @author swapnadeep.dutta
 * 
 * This class is for fetching and storing the information regarding the customer authentication
 */

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "customer_auth")
@NamedQueries({
		@NamedQuery(name = "customerAuthTokenByAccessToken", query = "select ct from CustomerAuthEntity ct where ct.accessToken = :accessToken") })
public class CustomerAuthEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "uuid")
	@Size(max = 200)
	@NonNull
	private String uuid;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "customer_id")
	private CustomerEntity customer;

	@Column(name = "access_token")
	@Size(max = 500)
	@NotNull
	private String accessToken;

	@Column(name = "login_at")
	@NotNull
	private ZonedDateTime loginAt;

	@Column(name = "logout_at")
	private ZonedDateTime loginOut;

	@Column(name = "expires_at")
	@NotNull
	private ZonedDateTime expiresAt;

	// No-argument Constructor for empty initializing

	public CustomerAuthEntity() {

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

	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public ZonedDateTime getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(ZonedDateTime loginAt) {
		this.loginAt = loginAt;
	}

	public ZonedDateTime getLoginOut() {
		return loginOut;
	}

	public void setLoginOut(ZonedDateTime loginOut) {
		this.loginOut = loginOut;
	}

	public ZonedDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(ZonedDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

}