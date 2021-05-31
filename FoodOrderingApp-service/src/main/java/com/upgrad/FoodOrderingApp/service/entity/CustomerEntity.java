package com.upgrad.FoodOrderingApp.service.entity;

/**
 * @author swapnadeep.dutta
 * 
 * This class is for fetching and storing the information regarding the customer
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.*;

@Entity
@Table(name = "customer")
@NamedQueries({
		@NamedQuery(name = "customerByContactNumber", query = "select c from CustomerEntity c where c.contactNumber = :contactNumber") })
public class CustomerEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "uuid")
	@Size(max = 200)
	@NotNull
	private String uuid;

	@Column(name = "firstname")
	@Size(max = 30)
	@NotNull
	private String firstName;

	@Column(name = "lastname")
	@Size(max = 30)
	@NotNull
	private String lastName;

	@Column(name = "email")
	@Size(max = 50)
	@NotNull
	@Email(message = "Please enter a valid email address")
	private String email;

	@Column(name = "contact_number")
	@Size(max = 30)
	@NotNull
	private String contactNumber;

	@Column(name = "password")
	@Size(max = 255)
	@NotNull
	@ToStringExclude
	private String password;

	@Column(name = "salt")
	@Size(max = 255)
	@NotNull
	@ToStringExclude
	private String salt;

	// No-argument Constructor for empty initializing

	public CustomerEntity() {

	}

	// Initialized constructor

	public CustomerEntity(long id, @Size(max = 200) @NotNull String uuid, @Size(max = 30) @NotNull String firstName,
			@Size(max = 30) @NotNull String lastName,
			@Size(max = 50) @NotNull @Email(message = "Please enter a valid email address") String email,
			@Size(max = 30) @NotNull String contactNumber, @Size(max = 255) @NotNull String password,
			@Size(max = 255) @NotNull String salt) {
		this.id = id;
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.contactNumber = contactNumber;
		this.password = password;
		this.salt = salt;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public boolean equals(Object obj) {
		return new EqualsBuilder().append(this, obj).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this).hashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}