package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

//This class is to interact with database for accessing the end points related to address controller

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method is to fetch the address on basis of address uuid
     * @param addressUuid for fetching address
     * @return all address by corresponding uuid or null if the uuid is not present in db
     */

    public AddressEntity getAddressByUUID(final String addressUuid) {
        try {
            return entityManager.createNamedQuery("addressByUuid", AddressEntity.class).setParameter("uuid", addressUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method is for creating new Address
     * @param addressEntity for creating new address
     * @return newly created address details
     */

    public AddressEntity createAddress(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    /**
     *This method corresponds to delete an address
     * @param addressEntity to remove the particular address entity
     * @return address entity
     */

    public AddressEntity deleteAddress(final AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    /**
     *This method is to update the Address
     * @param addressEntity whose details are to be updated
     * @return updated address entity
     */

    public AddressEntity updateAddress(final AddressEntity addressEntity) {
        return entityManager.merge(addressEntity);
    }

    /**
     * This method is to get address of particular customer using customer id
     * @param customer whose address are to be fetched
     * @return addresses
     */

    public List<CustomerAddressEntity> customerAddressByCustomer(CustomerEntity customer) {
        List<CustomerAddressEntity> addresses =
                entityManager.createNamedQuery("customerAddressesByCustomerId", CustomerAddressEntity.class).setParameter("customer", customer)
                        .getResultList();
        if (addresses == null) {
            return Collections.emptyList();
        }
        return addresses;
    }
}