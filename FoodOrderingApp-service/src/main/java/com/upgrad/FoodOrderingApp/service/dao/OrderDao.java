package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderDao {
    @PersistenceContext private EntityManager entityManager;

    /**
     * Fetches the orders of the customer in a sorted manner with latest order being on the top.
     * @param customerUUID customer whose orders are to be fetched. * @return list of orders made by
     *     customer
     * @return list of orders made by customer.
     */
    public List<OrderEntity> getOrdersByCustomers(String customerUUID) {
        List<OrderEntity> ordersByCustomer = entityManager.createNamedQuery("getOrdersByCustomer", OrderEntity.class).setParameter("customerUUID", customerUUID)
                        .getResultList();
        if (ordersByCustomer != null) {
            return ordersByCustomer;
        }
        return Collections.emptyList();
    }
    public OrderEntity saveOrder(OrderEntity orderEntity) {
        entityManager.persist(orderEntity);
        return orderEntity;
    }


    public OrderItemEntity saveOrderItem(OrderItemEntity orderedItem) {
        entityManager.persist(orderedItem);
        return orderedItem;
    }
    /**
     *List all the Order based on Restaurant
     *
     **/
    public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurant) {
        try {
            return entityManager.createNamedQuery("getOrdersByRestaurant", OrderEntity.class)
                    .setParameter("restaurant", restaurant).getResultList();
        } catch (NoResultException nre) {
            System.out.printf("GetOrderRestaurant");
            return null;
        }
    }



    }

