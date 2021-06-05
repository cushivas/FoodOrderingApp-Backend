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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

//This Class represents the CustomerAuth table in the DB.

@Entity
@Table(name = "customer_auth", uniqueConstraints = { @UniqueConstraint(columnNames = { "uuid" }) })
@NamedQueries({
		@NamedQuery(name = "getCustomerAuthByAccessToken", query = "SELECT c from CustomerAuthEntity c where c.accessToken = :access_Token"), })
public class CustomerAuthEntity implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "uuid")
	@Size(max = 200)
	@NotNull
	private String uuid;

	@ManyToOne()
	@JoinColumn(name = "customer_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private CustomerEntity customer;

	@Column(name = "access_token")
	@Size(max = 500)
	private String accessToken;

	@Column(name = "login_at")
	private ZonedDateTime loginAt;

	@Column(name = "logout_at")
	private ZonedDateTime logoutAt;

	@Column(name = "expires_at")
	private ZonedDateTime expiresAt;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
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

	public ZonedDateTime getLogoutAt() {
		return logoutAt;
	}

	public void setLogoutAt(ZonedDateTime logoutAt) {
		this.logoutAt = logoutAt;
	}

	public ZonedDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(ZonedDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
}
