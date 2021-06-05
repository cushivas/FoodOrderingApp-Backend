package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;

import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;


    /**
     * Method takes customers access token and retrieves a list of all of the customer's orders
     *
     * @param authorization Customer's access token as request header parameter
     * @return ResponseEntity with list of all of the customer's orders
     * @throws AuthorizationFailedException on invalid/incorrect access token
     */
    @CrossOrigin
    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerOrderResponse> getOrders(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        // Get Bearer Authorization Token
        String[] accessToken = authorization.split("Bearer ");
        if (accessToken.length != 2) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        CustomerEntity customerEntity = customerService.getCustomer(accessToken[1]);


        CustomerOrderResponse response = new CustomerOrderResponse();

        // Get orders for customer as OrderEntities
        List<OrderEntity> orderEntities = orderService.getOrdersByCustomers(customerEntity.getUuid());

        // Map order entities to order list response
        orderEntities.forEach(orderEntity -> {
            OrderList orderList = new OrderList()
                    .id(UUID.fromString(orderEntity.getUuid()))
                    .customer(new OrderListCustomer()
                            .id(UUID.fromString(orderEntity.getCustomer().getUuid()))
                            .firstName(orderEntity.getCustomer().getFirstName())
                            .lastName(orderEntity.getCustomer().getLastName())
                            .emailAddress(orderEntity.getCustomer().getEmail())
                            .contactNumber(orderEntity.getCustomer().getContactNumber()))
                    .bill(BigDecimal.valueOf(orderEntity.getBill()))
                    .discount(BigDecimal.valueOf(orderEntity.getDiscount()))
                    .date(orderEntity.getDate().toString())
                    .address(new OrderListAddress()
                            .id(UUID.fromString(orderEntity.getAddress().getUuid()))
                            .flatBuildingName(orderEntity.getAddress().getUuid())
                            .locality(orderEntity.getAddress().getLocality())
                            .city(orderEntity.getAddress().getCity())
                            .pincode(orderEntity.getAddress().getPincode()));
            if (orderEntity.getAddress().getState() != null) {
                orderList.getAddress().state(new OrderListAddressState()
                        .id(UUID.fromString(orderEntity.getAddress().getState().getUuid()))
                        .stateName(orderEntity.getAddress().getState().getStateName()));
            }
            if (orderEntity.getCoupon() != null) {
                orderList.coupon(new OrderListCoupon()
                        .id(UUID.fromString(orderEntity.getCoupon().getUuid()))
                        .couponName(orderEntity.getCoupon().getCouponName())
                        .percent(orderEntity.getCoupon().getPercent()));
            }
            if (orderEntity.getPayment() != null) {
                orderList.payment(new OrderListPayment()
                        .id(UUID.fromString(orderEntity.getPayment().getUuid()))
                        .paymentName(orderEntity.getPayment().getPaymentName()));
            }
            orderEntity.getOrderItems().forEach(orderItemEntity -> {
                orderList.addItemQuantitiesItem(new ItemQuantityResponse()
                        .item(new ItemQuantityResponseItem()
                                .id(UUID.fromString(orderItemEntity.getItem().getUuid()))
                                .itemName(orderItemEntity.getItem().getItemName())
                                .itemPrice(orderItemEntity.getItem().getPrice())
                                .type(ItemQuantityResponseItem.TypeEnum.fromValue(orderItemEntity.getItem().getType().toString())))
                        .price(orderItemEntity.getPrice())
                        .quantity(orderItemEntity.getQuantity()));
            });
            response.addOrdersItem(orderList);
        });

        // Return response with right HttpStatus
        if (response.getOrders() == null || response.getOrders().isEmpty()) {
            return new ResponseEntity<CustomerOrderResponse>(response, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<CustomerOrderResponse>(response, HttpStatus.OK);
        }
    }

    /**
     * @param authorization customer who owns the coupon
     * @param couponName    name of the coupon
     * @return couponDetailResponse
     * @throws AuthorizationFailedException if customer validation fails
     * @throws CouponNotFoundException      if coupon name does not exist in database
     */
    @CrossOrigin
    @RequestMapping(path = "/coupon/{coupon_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCoupon(@RequestHeader("authorization") final String authorization, @PathVariable("coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        String[] accessToken = authorization.split("Bearer ");
        if (accessToken.length != 2) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        CustomerEntity customerEntity = customerService.getCustomer(accessToken[1]);

        // Get coupon details from the database (By Name)
        final CouponEntity coupon = orderService.getCouponByCouponName(couponName);

        // Map coupon details to coupon details response
        CouponDetailsResponse response = new CouponDetailsResponse();
        response.id(UUID.fromString(coupon.getUuid())).couponName(coupon.getCouponName()).percent(coupon.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(response, HttpStatus.OK);
    }

}