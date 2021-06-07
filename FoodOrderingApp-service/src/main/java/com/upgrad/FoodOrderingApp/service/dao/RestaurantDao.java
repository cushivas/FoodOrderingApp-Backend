package com.upgrad.FoodOrderingApp.service.dao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *  Database access class for restaurant related operations
 */

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     *  Get list of all restaurants by rating..
     * @return list of restaurants
     */

    public List<RestaurantEntity> restaurantsByRating(){
        try{
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("restaurantsByRating",RestaurantEntity.class).getResultList();
            return restaurantEntities;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Get current restaurant details
     * @param uuid restaurant uuid
     * @return List of Restaurant details for current uuid..
     */

    public RestaurantEntity getRestaurantByUuid(String uuid) {
        try {
            RestaurantEntity restaurantEntity = entityManager.createNamedQuery("getRestaurantByUuid",RestaurantEntity.class).setParameter("uuid",uuid).getSingleResult();
            return restaurantEntity;
        }catch (NoResultException nre){
            return null;
        }

    }

    /**
     * Get current restaurant details by name
     * @param restaurantName restaurant name
     * @return List of Restaurant details for current restaurant name..
     */

    public List<RestaurantEntity> restaurantsByName(String restaurantName) {
        try {
            String restaurantNameLow = "%"+restaurantName.toLowerCase()+"%";
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("restaurantsByName", RestaurantEntity.class).setParameter("restaurant_name_low",restaurantNameLow).getResultList();
            return restaurantEntities;
        }catch (NoResultException nre){
            return null;
        }

    }

    /**
     *  Method to update restaurant rating in db
     * @param restaurantEntity
     * @return
     */

    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity) {
        entityManager.merge(restaurantEntity);
        return restaurantEntity;
    }
}
