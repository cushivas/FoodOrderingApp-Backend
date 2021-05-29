package com.upgrad.FoodOrderingApp.service.businness.service.impl;

import java.time.ZonedDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.FoodOrderingApp.service.businness.JwtTokenProvider;
import com.upgrad.FoodOrderingApp.service.businness.PasswordCryptographyProvider;
import com.upgrad.FoodOrderingApp.service.businness.service.CustomerService;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private PasswordCryptographyProvider passwordCryptographyProvider;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerAuthEntity authenticateCustomer(String contactNumber, String password)
			throws AuthenticationFailedException {
		if (contactNumber.isEmpty() || password.isEmpty()) {
			throw new AuthenticationFailedException("ATH-003",
					"Incorrect format of decoded customer name and password");
		}
		CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactNumber);
		if (customerEntity == null) {
			throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
		}
		final String encryptedPassword = passwordCryptographyProvider.encrypt(password, customerEntity.getSalt());
		if (encryptedPassword.equals(customerEntity.getPassword())) {
			JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
			final ZonedDateTime now = ZonedDateTime.now();
			final ZonedDateTime expiresAt = now.plusHours(8);
			final String authToken = jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt);
			CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
			customerAuthEntity.setAccessToken(authToken);
			customerAuthEntity.setCustomerEntity(customerEntity);
			customerAuthEntity.setLoginAt(now);
			customerAuthEntity.setExpiresAt(expiresAt);
			customerAuthEntity.setUuid(UUID.randomUUID().toString());
			customerDao.createAuthenticationToken(customerAuthEntity);
			customerDao.updateCustomer(customerEntity);
			return customerAuthEntity;
		} else {
			throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity logout(String accessToken) throws AuthorizationFailedException {
		CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthToken(accessToken);
		if (customerAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "Customer is not logged in.");
		} else if (customerAuthEntity != null && customerAuthEntity.getLoginOut() != null) {
			throw new AuthorizationFailedException("ATHR-002",
					"Customer is logged out. Log in again to access this endpoint.");
		} else if (customerAuthEntity != null && ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
			throw new AuthorizationFailedException("ATHR-003",
					"Your session is expired. Log in again to access this endpoint.");
		} else {
			final ZonedDateTime now = ZonedDateTime.now();
			customerAuthEntity.setLoginOut(now);
			return customerAuthEntity.getCustomerEntity();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
		CustomerAuthEntity customerAuthEntity = null;
		if (accessToken != null) {
			customerAuthEntity = customerDao.getCustomerAuthToken(accessToken);
		} else {
			throw new AuthorizationFailedException("ATHR-004", "Access token cannot be null");
		}
		if (customerAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "Customer is not logged in");
		} else if (customerAuthEntity != null && customerAuthEntity.getLoginOut() != null) {
			throw new AuthorizationFailedException("ATHR-002",
					"Customer is logged out. Log in again to access this endpoint.");
		} else if (customerAuthEntity != null && ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
			throw new AuthorizationFailedException("ATHR-003",
					"Your session is expired. Log in again to access to access this endpoint.");
		} else {
			return customerAuthEntity.getCustomerEntity();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity updateCustomer(CustomerEntity customerEntity) throws UpdateCustomerException {
		customerDao.updateCustomer(customerEntity);
		return customerEntity;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity updateCustomerPassword(String oldPassword, String newPassword, CustomerEntity customerEntity)
			throws UpdateCustomerException {
		try {
			oldPassword.isEmpty();
			newPassword.isEmpty();
		} catch (Exception e) {
			throw new UpdateCustomerException("UCR-003", "No field should be empty");
		}

		if (!passwordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt())
				.equals(customerEntity.getPassword())) {
			throw new UpdateCustomerException("UCR-004", "Incorrect old password");
		} else if (weakPassword(newPassword)) {
			throw new UpdateCustomerException("UCR-002", "Weak Password!");
		} else {
			String[] encryptedPasswords = this.passwordCryptographyProvider.encrypt(newPassword);
			customerEntity.setSalt(encryptedPasswords[0]);
			customerEntity.setPassword(encryptedPasswords[1]);
			customerDao.updateCustomer(customerEntity);
			return customerEntity;
		}
	}

	@Override
	public boolean weakPassword(String password) {
		boolean weakPassword = true;
		if (password.length() >= 8) {
			if (Pattern.matches(".*[0-9].*", password)) {
				if (Pattern.matches(".*[A-Z].*", password)) {
					if (Pattern.matches(".*[a-z].*", password)) {
						if (Pattern.matches(".*[#@$%&*!^].*", password)) {
							weakPassword = false;
						}
					}
				}
			}
		}
		return weakPassword;
	}

}
