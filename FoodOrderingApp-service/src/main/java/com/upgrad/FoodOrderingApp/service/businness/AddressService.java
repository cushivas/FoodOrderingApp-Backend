package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.OrdersDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

	@Autowired
	private StateDao stateDao;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private CustomerAddressDao customerAddressDao;

	@Autowired
	private OrdersDao ordersDao;

	/**
	 *
	 * @param addressEntity  for saving all the details of address
	 * @param customerEntity for storing the address details for particular customer
	 * @return return the entered details as an address Entity
	 * @throws SaveAddressException if any validation fail
	 */

	@Transactional(propagation = Propagation.REQUIRED)
	public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerEntity customerEntity)
			throws SaveAddressException {
		if (addressEntity.getActive() != null && addressEntity.getLocality() != null
				&& !addressEntity.getLocality().isEmpty() && addressEntity.getCity() != null
				&& addressEntity.getCity().isEmpty() && addressEntity.getFlatBuildingNumber() != null
				&& !addressEntity.getFlatBuildingNumber().isEmpty() && addressEntity.getPincode() != null
				&& addressEntity.getPincode().isEmpty() && addressEntity.getState() != null) {
			if (!isValidPincode(addressEntity.getPincode())) {
				throw new SaveAddressException("SAR-002", "Invalid pincode");
			}

			AddressEntity customerAddress = addressDao.createAddress(addressEntity);
			CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
			customerAddressEntity.setCustomer(customerEntity);
			customerAddressEntity.setAddress(customerAddress);
			customerAddressDao.saveCustomerAddress(customerAddressEntity);
			return customerAddress;
		} else {
			throw new SaveAddressException("SAR_001", "No field can be empty");
		}
	}

	/**
	 *
	 * @param pincode to check if the pincode entered by user is valid or not
	 * @return boolean true / false value
	 */

	private boolean isValidPincode(final String pincode) {
		if (pincode.length() != 6) {
			return false;
		}
		for (int i = 0; i < pincode.length(); i++) {
			if (!Character.isDigit(pincode.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param addressId      it is used to check the validation condition
	 * @param customerEntity customer whose address is to be checked
	 * @return addressEntity if the customer data is valid and exist in data base
	 * @throws AuthorizationFailedException if the customer is not authorized
	 * @throws AddressNotFoundException     if the address entered is not exist in
	 *                                      database
	 */

	public AddressEntity getAddressByUUID(final String addressId, final CustomerEntity customerEntity)
			throws AuthorizationFailedException, AddressNotFoundException {
		AddressEntity addressEntity = addressDao.getAddressByUUID(addressId);
		if (addressId.isEmpty()) {
			throw new AddressNotFoundException("ANF-005", "Address id cannot be empty");
		}
		if (addressEntity == null) {
			throw new AddressNotFoundException("ANF-003", "No address by this id");
		}
		CustomerAddressEntity customerAddressEntity = customerAddressDao.getCustomerAddressByAddress(addressEntity);
		if (!customerAddressEntity.getCustomer().getUuid().equals(customerEntity.getUuid())) {
			throw new AuthorizationFailedException("ATHR-004",
					"You are not authorized to view/update/delete any one else's address");
		}
		return addressEntity;
	}

	/**
	 *
	 * @param stateUuid to fetch the state by uuid
	 * @return required state
	 * @throws AddressNotFoundException if the state with entered id is not found in
	 *                                  database
	 */

	public StateEntity getStateByUUID(final String stateUuid) throws AddressNotFoundException {
		if (stateDao.getStateByUUID(stateUuid) == null) {
			throw new AddressNotFoundException("ANF-002", "No state by this id");
		}
		return stateDao.getStateByUUID(stateUuid);
	}

	/**
	 *
	 * @param addressEntity the address which is to be deleted
	 * @return updated address
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public AddressEntity deleteAddress(final AddressEntity addressEntity) {
		final List<OrderEntity> orders = ordersDao.getAllOrdersByAddress(addressEntity);
		if (orders == null || orders.isEmpty()) {
			return addressDao.deleteAddress(addressEntity);
		}
		addressEntity.setActive(0);
		return addressDao.updateAddress(addressEntity);
	}

	/**
	 *
	 * @param customerEntity get all the address for the customer
	 * @return list of address
	 */

	public List<AddressEntity> getAllAddress(final CustomerEntity customerEntity) {
		List<AddressEntity> addressEntityList = new ArrayList<>();
		List<CustomerAddressEntity> customerAddressEntityList = addressDao.customerAddressByCustomer(customerEntity);
		if (customerAddressEntityList != null || !customerAddressEntityList.isEmpty()) {
			customerAddressEntityList
					.forEach(customerAddressEntity -> addressEntityList.add(customerAddressEntity.getAddress()));
		}
		return addressEntityList;
	}

	/**
	 * This method is GET method and simply returns all states from db
	 * 
	 * @return list of states
	 */
	public List<StateEntity> getAllStates() {
		return stateDao.getAllStates();
	}
}