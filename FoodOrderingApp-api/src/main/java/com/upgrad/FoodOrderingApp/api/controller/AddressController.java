package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
//This is the Address Controller class
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    /**
     * @param authorization      for checking if user is authorised to save address
     * @param saveAddressRequest for saving details of new address
     * @return saveAddressResponse entity
     * @throws AuthorizationFailedException if user is not valid user
     * @throws SaveAddressException         if validation details are not fullfilled
     * @throws AddressNotFoundException     if address not found
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader(value = "authorization", required = true) final String authorization,
                                                           @RequestBody(required = false) final SaveAddressRequest saveAddressRequest)
            throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        String[] accessToken = authorization.split("Bearer ");
        if (accessToken.length != 2) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        CustomerEntity customerEntity = customerService.getCustomer(accessToken[1]);
        final AddressEntity addressEntity = new AddressEntity();
        if (saveAddressRequest != null) {
            addressEntity.setUuid(UUID.randomUUID().toString());
            addressEntity.setCity(saveAddressRequest.getCity());
            addressEntity.setLocality(saveAddressRequest.getLocality());
            addressEntity.setPincode(saveAddressRequest.getPincode());
            addressEntity.setFlatBuildingNumber(saveAddressRequest.getFlatBuildingName());
            addressEntity.setActive(1);
        }
        addressEntity.setState(addressService.getStateByUUID(saveAddressRequest.getStateUuid()));

        final AddressEntity savedAddress = addressService.saveAddress(addressEntity, customerEntity);
        SaveAddressResponse saveAddressResponse =
                new SaveAddressResponse()
                        .id(savedAddress.getUuid())
                        .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);

    }

    /**
     *
     * @param authorization to check valid customer
     * @param addressId whose address is to be deleted
     * @return DeleteAddressResponse
     * @throws AuthorizationFailedException if customer is not authorized
     * @throws AddressNotFoundException if address does not exist
     */

    @CrossOrigin
    @RequestMapping(path = "/address/{address_id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("address_id") final String addressId)
            throws AuthorizationFailedException, AddressNotFoundException {

        String[] accessToken = authorization.split("Bearer ");
        if (accessToken.length != 2) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken[1]);

        AddressEntity addressEntity = addressService.getAddressByUUID(addressId, customerEntity);
        final AddressEntity deletedAddressEntity = new AddressEntity();
        deletedAddressEntity.setUuid(UUID.randomUUID().toString());
        final AddressEntity deleteAddress = addressService.deleteAddress(addressEntity);
        final DeleteAddressResponse deleteAddressResponse =
                new DeleteAddressResponse()
                        .id(UUID.fromString(deleteAddress.getUuid()))
                        .status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }

    /**
     *
     * @param authorization to validate customer
     * @return AddressListResponse
     * @throws AuthorizationFailedException if validation details are not met
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddress(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        String[] accessToken = authorization.split("Bearer ");
        if (accessToken.length != 2) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken[1]);


        final List<AddressEntity> addressEntityList = addressService.getAllAddress(customerEntity);

        final AddressListResponse addressListResponse = new AddressListResponse();

        if (!addressEntityList.isEmpty()) {
            for (AddressEntity addressEntity : addressEntityList) {
                AddressList addressResponseList = new AddressList().id(UUID.fromString(addressEntity.getUuid()))
                                .flatBuildingName(addressEntity.getFlatBuildingNumber())
                                .city(addressEntity.getCity())
                                .pincode(addressEntity.getPincode())
                                .locality(addressEntity.getLocality())
                                .state(new AddressListState().id(UUID.fromString(addressEntity.getState().getUuid()))
                                                .stateName(addressEntity.getState().getStateName()));
                addressListResponse.addAddressesItem(addressResponseList);
            }
        } else {
            List<AddressList> addresses = Collections.emptyList();
            addressListResponse.addresses(addresses);
        }

        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    /**
     * This method is to give all states details
     * @return stateListResponse
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/states")
    public ResponseEntity<StatesListResponse> getAllStates() {
        final StateEntity stateEntity = new StateEntity();
        stateEntity.setUuid(UUID.randomUUID().toString());

        final List<StateEntity> statesLists = addressService.getAllStates();

        final StatesListResponse statesListResponse = new StatesListResponse();
        for (StateEntity statesEntity : statesLists) {
            StatesList states =
                    new StatesList()
                            .id(UUID.fromString(statesEntity.getUuid()))
                            .stateName(statesEntity.getStateName());
            statesListResponse.addStatesItem(states);
        }
        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }
}



