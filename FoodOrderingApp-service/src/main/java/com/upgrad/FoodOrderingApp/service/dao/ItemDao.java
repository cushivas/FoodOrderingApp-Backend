package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method is used to get item by uuid
     *
     * @param uuid to fetch the item from uuid
     * @return item Entity
     */

    public ItemEntity getItemByUUID(String uuid) {
        try {
            ItemEntity itemEntity = entityManager.createNamedQuery("getItemByUUID", ItemEntity.class).setParameter("uuid", uuid).getSingleResult();
            return itemEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }

}
