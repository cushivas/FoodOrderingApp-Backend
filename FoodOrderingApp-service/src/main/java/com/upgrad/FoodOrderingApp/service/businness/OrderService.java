package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * This method contains business logic to get coupon details by coupon name.
     *
     * @param couponName it is a String
     * @return couponEntity
     * @throws CouponNotFoundException if coupon with that name doesn't exist in database.
     */
    public CouponEntity getCouponByCouponName(String couponName) throws CouponNotFoundException {

        if (couponName.isEmpty()) {
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }

        CouponEntity couponEntity = couponDao.getCouponByName(couponName.toUpperCase());

        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }

        return couponEntity;
    }


    /**
     * Fetches the orders of the customer in a sorted manner with latest order being on the top.
     *
     * @param customerUUID customer whose orders are to be fetched.
     * @return list of orders made by customer
     */
    public List<OrderEntity> getOrdersByCustomers(String customerUUID) {
        return orderDao.getOrdersByCustomers(customerUUID);
    }

    /**
     * @param uuid to fetch coupon by uuid
     * @return coupon entity
     * @throws CouponNotFoundException if the coupon for corresponding coupon id is not found
     */
    public CouponEntity getCouponByCouponId(String uuid) throws CouponNotFoundException {
        CouponEntity couponEntity = couponDao.getCouponByCouponId(uuid);
        if (couponEntity != null) {
            return couponEntity;
        } else
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
    }

    /**
     * method to save the details of order
     *
     * @param orderEntity which contain order details
     * @return save order entity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(OrderEntity orderEntity) {
        return orderDao.saveOrder(orderEntity);
    }

    /**
     *  Method to save order items
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(OrderItemEntity orderedItem) {
        return orderDao.saveOrderItem(orderedItem);
    }

}
