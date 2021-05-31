package com.upgrad.FoodOrderingApp.service.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Repository
public class CustomerAddressDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void createCustomerAddress(CustomerAddressEntity customerAddressEntity) {
		entityManager.persist(customerAddressEntity);
	}

	public CustomerAddressEntity getCustomerAddressByAddressId(AddressEntity addressEntity) {
		try {
			return this.entityManager.createNamedQuery("customerAddressByAddressId", CustomerAddressEntity.class)
					.setParameter("address", addressEntity).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<AddressEntity> getCustomerAddressListByCustomer(CustomerEntity customerEntity) {
		try {
			List<AddressEntity> addressEntities = new ArrayList<>();
			List<CustomerAddressEntity> customerAddressEntities = this.entityManager
					.createNamedQuery("customerAddressesByCustomerId", CustomerAddressEntity.class)
					.setParameter("customer", customerEntity).getResultList();
			for (CustomerAddressEntity customerAddressEntity : customerAddressEntities) {
				addressEntities.add(customerAddressEntity.getAddress());
			}
			return addressEntities;
		} catch (NoResultException e) {
			return null;
		}
	}

}