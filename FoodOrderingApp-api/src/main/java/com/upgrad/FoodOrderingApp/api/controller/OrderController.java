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

import java.time.ZonedDateTime;
import java.util.ArrayList;
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


@Autowired
private RestaurantService restaurantService;

@Autowired
private ItemService itemService;

@Autowired
private AddressService addressService;
@Autowired
private PaymentService paymentService;
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

    /**
     *
     * @param authorization to check for valid customer
     * @param saveOrderRequest to save the requested order
     * @return save order Response
     * @throws AuthorizationFailedException if the customer is not authorized
     * @throws AddressNotFoundException if the address of customer does not exist in data base
     * @throws RestaurantNotFoundException if restaurant is not found
     * @throws CouponNotFoundException if the valid coupon is not found
     * @throws PaymentMethodNotFoundException
     * @throws ItemNotFoundException
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader("authorization") final String authorization,
                                                       @RequestBody(required = false) final SaveOrderRequest saveOrderRequest)
            throws AuthorizationFailedException, AddressNotFoundException, RestaurantNotFoundException, CouponNotFoundException, PaymentMethodNotFoundException, ItemNotFoundException {
        String[] accessToken = authorization.split("Bearer ");
        if (accessToken.length != 2) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        CustomerEntity customerEntity = customerService.getCustomer(accessToken[1]);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUuid(UUID.randomUUID().toString());
        orderEntity.setBill(saveOrderRequest.getBill().doubleValue());
        if (saveOrderRequest.getCouponId() != null) {
            CouponEntity couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());
            orderEntity.setCoupon(couponEntity);
        } else {
            orderEntity.setCoupon(null);
        }
        if (saveOrderRequest.getDiscount() != null) {
            orderEntity.setDiscount(saveOrderRequest.getDiscount().doubleValue());
        } else {
            orderEntity.setDiscount(BigDecimal.ZERO.doubleValue());
        }
        orderEntity.setDate(ZonedDateTime.now());
        if (saveOrderRequest.getPaymentId() != null) {
            PaymentEntity paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
            orderEntity.setPayment(paymentEntity);
        } else {
            orderEntity.setPayment(null);
        }
        orderEntity.setCustomer(customerEntity);
        AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId(), customerEntity);
        orderEntity.setAddress(addressEntity);
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());
        orderEntity.setRestaurant(restaurantEntity);
        OrderEntity savedOrderEntity = orderService.saveOrder(orderEntity);
        List<ItemQuantity> itemQuantities = saveOrderRequest.getItemQuantities();
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        for (ItemQuantity itemQuantity : itemQuantities) {
            OrderItemEntity orderedItem = new OrderItemEntity();
            ItemEntity itemEntity = itemService.getItemByUUID(itemQuantity.getItemId().toString());
            orderedItem.setOrder(savedOrderEntity);
            orderedItem.setItem(itemEntity);
            orderedItem.setQuantity(itemQuantity.getQuantity());
            orderedItem.setPrice(itemQuantity.getPrice());
            orderItemEntities.add(orderedItem);
            orderService.saveOrderItem(orderedItem);
        }
        SaveOrderResponse saveOrderResponse = new SaveOrderResponse();
        saveOrderResponse.setStatus("ORDER SUCCESSFULLY PLACED");
        saveOrderResponse.setId(savedOrderEntity.getUuid());
        return new ResponseEntity<>(saveOrderResponse, HttpStatus.CREATED);
    }




}