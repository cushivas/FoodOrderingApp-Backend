package com.upgrad.FoodOrderingApp.service.dao;

import java.util.List;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

public interface CustomerAddressDao {

	public CustomerAddressEntity getCustomerAddressByAddressId(AddressEntity addressEntity);

	public List<AddressEntity> getCustomerAddressListByCustomer(CustomerEntity customerEntity);

}
