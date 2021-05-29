package com.upgrad.FoodOrderingApp.api.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.LogoutResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdatePasswordRequest;
import com.upgrad.FoodOrderingApp.api.model.UpdatePasswordResponse;
import com.upgrad.FoodOrderingApp.service.businness.service.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

@RestController
@CrossOrigin
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@RequestMapping(path = "/customer/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SignupCustomerResponse> signup(final SignupCustomerRequest signupCustomerRequest)
			throws SignUpRestrictedException {
		final CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setUuid(UUID.randomUUID().toString());
		try {
			signupCustomerRequest.getFirstName().isEmpty();
			signupCustomerRequest.getLastName().isEmpty();
			signupCustomerRequest.getEmailAddress().isEmpty();
			signupCustomerRequest.getPassword().isEmpty();
		} catch (Exception e) {
			throw new SignUpRestrictedException("SGR-005", "Except last name, all the fields should be fileld");
		}
		customerEntity.setFirstName(signupCustomerRequest.getFirstName());
		customerEntity.setLastName(signupCustomerRequest.getLastName());
		customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
		customerEntity.setPassword(signupCustomerRequest.getPassword());
		customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
		customerEntity.setSalt("1234abc");
		final CustomerEntity entity = customerService.saveCustomer(customerEntity);
		SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(entity.getUuid())
				.status("Customer Successfully Registered");
		return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.OK);
	}

	@RequestMapping(path = "/customer/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<LoginResponse> login(@RequestHeader("authentication") final String authentication)
			throws AuthenticationFailedException {
		byte[] decoded = null;
		String[] decodedArray = null;
		try {
			decoded = Base64.getDecoder().decode(authentication.split("Basic ")[1]);
		} catch (Exception e) {
			throw new AuthenticationFailedException("ATH-003",
					"Incorrect format of decoded customer name and password");
		}
		String decodedText = new String(decoded);
		try {
			String temp = decodedText.split(":")[1];
			decodedArray = decodedText.split(":");
		} catch (Exception e) {
			throw new AuthenticationFailedException("ATH-003",
					"Incorrect format of decoded customer name and password");
		}
		final CustomerAuthEntity customerAuthEntity = customerService.authenticateCustomer(decodedArray[0],
				decodedArray[1]);
		CustomerEntity customerEntity = customerAuthEntity.getCustomerEntity();
		LoginResponse loginResponse = new LoginResponse().firstName(customerEntity.getFirstName())
				.lastName(customerEntity.getLastName()).emailAddress(customerEntity.getEmail())
				.contactNumber(customerEntity.getContactNumber()).id(customerEntity.getUuid())
				.message("Logged In Successfully");
		HttpHeaders httpHeaders = new HttpHeaders();
		List<String> headers = new ArrayList<>();
		headers.add("access-token");
		httpHeaders.setAccessControlExposeHeaders(headers);
		httpHeaders.add("access-token", customerAuthEntity.getAccessToken());
		return new ResponseEntity<LoginResponse>(loginResponse, httpHeaders, HttpStatus.OK);
	}

	@RequestMapping(path = "/customer/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String accessToken)
			throws AuthorizationFailedException {
		String[] bearerToken = accessToken.split("Bearer ");
		if (bearerToken.length == 1) {
			throw new AuthorizationFailedException("ATHR-005", "Use valid authorization format <Bearer accessToken>");
		} else {
			final CustomerEntity customerEntity = customerService.logout(bearerToken[1]);
			LogoutResponse logoutResponse = new LogoutResponse().id(customerEntity.getUuid())
					.message("Logged out successfully");
			return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
		}
	}

	@RequestMapping(path = "/customer", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<UpdateCustomerResponse> updateCustomer(final UpdateCustomerRequest updateCustomerRequest,
			@RequestHeader("authorization") final String accessToken)
			throws AuthenticationFailedException, UpdateCustomerException, AuthorizationFailedException {
		String[] bearerToken = accessToken.split("Bearer ");
		CustomerEntity customerEntity = null;
		if (bearerToken.length == 1) {
			throw new AuthenticationFailedException("ATHR-005", "Use valid authorization format <Bearer accessToken>");
		} else {
			customerEntity = customerService.getCustomer(bearerToken[1]);
		}
		try {
			updateCustomerRequest.getFirstName().isEmpty();
		} catch (Exception e) {
			throw new UpdateCustomerException("UCR-002", "First name should not be empty");
		}
		customerEntity.setFirstName(updateCustomerRequest.getFirstName());
		customerEntity.setLastName(updateCustomerRequest.getLastName());
		final CustomerEntity entity = customerService.updateCustomer(customerEntity);
		UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(entity.getUuid())
				.firstName(entity.getFirstName()).lastName(entity.getLastName())
				.status("Customer Details updated successfully");
		return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
	}

	public ResponseEntity<UpdatePasswordResponse> updatePassword(final UpdatePasswordRequest updatePasswordRequest,
			@RequestHeader("authorization") final String accessToken)
			throws UpdateCustomerException, AuthorizationFailedException {
		String[] bearerToken = accessToken.split("Bearer ");
		CustomerEntity customerEntity = null;
		if (bearerToken.length == 1) {
			throw new AuthorizationFailedException("ATHR-005", "Use valid authorization format <Bearer accessToken>");
		} else {
			customerEntity = customerService.getCustomer(bearerToken[1]);
		}
		CustomerEntity entity = customerService.updateCustomerPassword(updatePasswordRequest.getOldPassword(),
				updatePasswordRequest.getNewPassword(), customerEntity);
		UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(entity.getUuid())
				.status("Customer Password udpated successfully");
		return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
	}

}
