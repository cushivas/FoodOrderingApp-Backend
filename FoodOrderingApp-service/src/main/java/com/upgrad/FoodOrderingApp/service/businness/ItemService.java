package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.Utility;
import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ItemService {

    @Autowired
    ItemDao itemDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    RestaurantItemDao restaurantItemDao;

    @Autowired
    CategoryItemDao categoryItemDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    Utility utility;

    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUuid, String categoryUuid) {

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);

        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryUuid);

        List<RestaurantItemEntity> restaurantItemEntities = restaurantItemDao.getItemsByRestaurant(restaurantEntity);

        List<CategoryItemEntity> categoryItemEntities = categoryItemDao.getItemsByCategory(categoryEntity);

        List<ItemEntity> itemEntities = new LinkedList<>();

        restaurantItemEntities.forEach(restaurantItemEntity -> {
            categoryItemEntities.forEach(categoryItemEntity -> {
                if(restaurantItemEntity.getItem().equals(categoryItemEntity.getItem())){
                    itemEntities.add(restaurantItemEntity.getItem());
                }
            });
        });

        return itemEntities;
    }

    public List<ItemEntity> getItemsByCategory(CategoryEntity categoryEntity) {
        List<CategoryItemEntity> categoryItemEntities = categoryItemDao.getItemsByCategory(categoryEntity);
        List<ItemEntity> itemEntities = new LinkedList<>();
        categoryItemEntities.forEach(categoryItemEntity -> {
            ItemEntity itemEntity = categoryItemEntity.getItem();
            itemEntities.add(itemEntity);
        });
        return itemEntities;
    }

    public ItemEntity getItemByUUID(String itemUuid) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.getItemByUUID(itemUuid);
        if(itemEntity == null){
            throw new ItemNotFoundException("INF-003","No item by this id exist");
        }
        return itemEntity;
    }

    /* This method is to get Items By Popularity and returns list of ItemEntity it takes restaurantEntity as input.*/

    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {

        //Calls getOrdersByRestaurant method of orderDao to get the  OrdersEntity
       List <OrdersEntity> ordersEntities = orderDao.getOrdersByRestaurant(restaurantEntity); 

       //Creating list of ItemEntity which are ordered from the restaurant.
       List <ItemEntity> itemEntities = new LinkedList<>();

        //Looping in for each ordersEntity in ordersEntities to get the corresponding orders
       ordersEntities.forEach(ordersEntity -> {
           //Calls getItemsByOrders method of orderItemDao to get the  OrderItemEntity
           List <OrderItemEntity> orderItemEntities = orderItemDao.getItemsByOrders(ordersEntity);
           orderItemEntities.forEach(orderItemEntity -> { //Looping in to get each tem from the OrderItemEntity.
               itemEntities.add(orderItemEntity.getItem());
           });
       });

       //Creating a HashMap to count the frequency of the order.
       Map<String,Integer> itemCountMap = new HashMap<String,Integer>();
       itemEntities.forEach(itemEntity -> { //Looping in to count the frequency of Item ordered correspondingly updating the count.
           Integer count = itemCountMap.get(itemEntity.getUuid());
           itemCountMap.put(itemEntity.getUuid(),(count == null) ? 1 : count+1);
       });

       //Calls sortMapByValues method of uitilityProvider and get sorted map by value.
       Map<String,Integer> sortedItemCountMap = utilityProvider.sortMapByValues(itemCountMap);

       //Creating the top 5 Itementity list
        List<ItemEntity> sortedItemEntites = new LinkedList<>();

        Integer count = 0;

        for(Map.Entry<String,Integer> item:sortedItemCountMap.entrySet()){
            if(count < 5) {
                //Calls getItemByUUID to get the Itemtentity
                sortedItemEntites.add(itemDao.getItemByUUID(item.getKey()));
                count = count+1;
            }else{
                break;
            }
        }

        return sortedItemEntites;
    }
}
