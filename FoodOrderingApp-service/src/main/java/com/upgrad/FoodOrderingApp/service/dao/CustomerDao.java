package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

public interface CustomerDao {

	public CustomerEntity createCustomer(CustomerEntity customerEntity);

	public CustomerEntity getCustomerByContactNumber(String contactNumber);

	public CustomerAuthEntity createAuthenticationToken(CustomerAuthEntity customerAuthEntity);

	public CustomerAuthEntity getCustomerAuthToken(final String accessToken);

	public void updateCustomer(CustomerEntity customerEntity);

}
