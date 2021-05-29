package com.upgrad.FoodOrderingApp.service.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Repository
public class CustomerAddressDaoImpl implements CustomerAddressDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CustomerAddressEntity getCustomerAddressByAddressId(AddressEntity addressEntity) {
		try {
			return this.entityManager.createNamedQuery("customerAddressByAddressId", CustomerAddressEntity.class)
					.setParameter("address", addressEntity).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<AddressEntity> getCustomerAddressListByCustomer(CustomerEntity customerEntity) {
		try {
			List<AddressEntity> addressEntities = new ArrayList<>();
			List<CustomerAddressEntity> customerAddressEntities = this.entityManager
					.createNamedQuery("customerAddressesByCustomerId", CustomerAddressEntity.class)
					.setParameter("customer", customerEntity).getResultList();
			for (CustomerAddressEntity customerAddressEntity : customerAddressEntities) {
				addressEntities.add(customerAddressEntity.getAddressEntity());
			}
			return addressEntities;
		} catch (NoResultException e) {
			return null;
		}
	}

}
