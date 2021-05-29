package com.upgrad.FoodOrderingApp.service.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;

@Repository
public class PaymentDaoImpl implements PaymentDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<PaymentEntity> getAllPaymentMethods() {
		try {
			return this.entityManager.createNamedQuery("allPaymentMethods", PaymentEntity.class).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public PaymentEntity getPaymentMethodById(String uuid) {
		try {
			return this.entityManager.createNamedQuery("getMethodById", PaymentEntity.class).setParameter("uuid", uuid)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
