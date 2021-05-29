package com.upgrad.FoodOrderingApp.service.businness.service;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

public interface CustomerService {

	public CustomerEntity saveCustomer(final CustomerEntity customerEntity) throws SignUpRestrictedException;

	public CustomerAuthEntity authenticateCustomer(final String contactNumber, final String password)
			throws AuthenticationFailedException;

	public CustomerEntity logout(final String accessToken) throws AuthorizationFailedException;

	public CustomerEntity getCustomer(final String accessToken) throws AuthorizationFailedException;

	public CustomerEntity updateCustomer(final CustomerEntity customerEntity) throws UpdateCustomerException;

	public CustomerEntity updateCustomerPassword(final String oldPassword, final String newPassword,
			final CustomerEntity customerEntity) throws UpdateCustomerException;

	public boolean weakPassword(String password);

}
