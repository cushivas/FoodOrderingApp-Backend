package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 *  Service class to handle all category related operations..
 */
@Service
public class CategoryService {
    @Autowired
    RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryDao categoryDao;

    /**
     *  Get all the categories inside one restaurant by restaurant Id..
     * @param restaurantUuid
     * @return
     */

    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantUuid){
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getCategoriesByRestaurant(restaurantEntity);
        List<CategoryEntity> categoryEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            categoryEntities.add(restaurantCategoryEntity.getCategory());
        });
        return categoryEntities;
    }

    /**
     *  Get all the categories for user
     * @return
     */

    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        List<CategoryEntity> categoryEntities = categoryDao.getAllCategoriesOrderedByName();
        return categoryEntities;
    }

    /**
     *  Get All the items inside category by Category Id.
     * @param categoryUuid -- Takes valid uuid
     * @return Category Entity..
     * @throws CategoryNotFoundException
     */

    public CategoryEntity getCategoryById(String categoryUuid) throws CategoryNotFoundException {

        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryUuid);
        // If category is null, throw exception
        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }

        return categoryEntity;
    }
}
