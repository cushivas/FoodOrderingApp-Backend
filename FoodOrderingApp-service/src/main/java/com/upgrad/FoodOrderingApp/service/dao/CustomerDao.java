package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Repository
public class CustomerDao {

	@PersistenceContext
	private EntityManager entityManager;

	public CustomerEntity createCustomer(CustomerEntity customerEntity) {
		this.entityManager.persist(customerEntity);
		return customerEntity;
	}

	public CustomerEntity getCustomerByContactNumber(String contactNumber) {
		try {
			return (CustomerEntity) this.entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class)
					.setParameter("contactNumber", contactNumber).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public CustomerAuthEntity createAuthenticationToken(final CustomerAuthEntity customerAuthEntity) {
		this.entityManager.persist(customerAuthEntity);
		return customerAuthEntity;
	}

	public CustomerAuthEntity getCustomerAuthToken(String accessToken) {
		try {
			return entityManager.createNamedQuery("customerAuthTokenByAccessToken", CustomerAuthEntity.class)
					.setParameter("accessToken", accessToken).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void updateCustomer(CustomerEntity customerEntity) {
		this.entityManager.merge(customerEntity);

	}

}