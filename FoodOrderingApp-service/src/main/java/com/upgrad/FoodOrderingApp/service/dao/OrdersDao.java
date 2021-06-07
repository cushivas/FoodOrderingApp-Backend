package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrdersDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch all the orders of a given address.
     *
     * @param addressEntity whose orders are to be fetched
     * @return List of OrderEntity.
     */
    public List<OrderEntity> getAllOrdersByAddress(final AddressEntity addressEntity) {
        return entityManager
                .createNamedQuery("allOrdersByAddress", OrderEntity.class)
                .setParameter("address", addressEntity)
                .getResultList();
    }

    /**
     * method to fetch items by order entity
     *
     * @param orderEntity whose related item is to be fetch
     * @return OrderItem entity
     */
    public List<OrderItemEntity> getItemsByOrders(OrderEntity orderEntity) {
        try {
            List<OrderItemEntity> orderItemEntities = entityManager.createNamedQuery("getItemsByOrders", OrderItemEntity.class).setParameter("ordersEntity", orderEntity).getResultList();
            return orderItemEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }
}