package com.upgrad.FoodOrderingApp.service.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Repository
public class CustomerDaoImpl implements CustomerDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CustomerEntity createCustomer(CustomerEntity customerEntity) {
		this.entityManager.persist(customerEntity);
		return customerEntity;
	}

	@Override
	public CustomerEntity getCustomerByContactNumber(String contactNumber) {
		try {
			return (CustomerEntity) this.entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class)
					.setParameter("contactNumber", contactNumber).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public CustomerAuthEntity createAuthenticationToken(CustomerAuthEntity customerAuthEntity) {
		this.entityManager.persist(customerAuthEntity);
		return customerAuthEntity;
	}

	@Override
	public CustomerAuthEntity getCustomerAuthToken(String accessToken) {
		try {
			return entityManager.createNamedQuery("customerAuthTokenByAccessToken", CustomerAuthEntity.class)
					.setParameter("accessToken", accessToken).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void updateCustomer(CustomerEntity customerEntity) {
		this.entityManager.merge(customerEntity);

	}

}
