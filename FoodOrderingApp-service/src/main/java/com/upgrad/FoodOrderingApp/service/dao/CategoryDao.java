package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *  Data Access Object to access db for category operation..
 */

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     *  Get Category by UUID..
     * @param uuid
     * @return
     */
    public CategoryEntity getCategoryByUuid(String uuid) {
        try {
            CategoryEntity categoryEntity = entityManager.createNamedQuery("getCategoryByUuid",CategoryEntity.class).setParameter("uuid",uuid).getSingleResult();
            return categoryEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     *  Get all the categories from db..
     * @return
     */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        try {
            List<CategoryEntity> categoryEntities = entityManager.createNamedQuery("getAllCategoriesOrderedByName",CategoryEntity.class).getResultList();
            return categoryEntities;
        }catch (NoResultException nre){
            return null;
        }
    }
}
