package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


//This class is to fetch the state entity details from data base
@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method is to get the state by using state uuid
     * @param stateUuid whose state name is to be fetched
     * @return states or null if state does not found
     */

    public StateEntity getStateByUUID(final String stateUuid) {
        try {
            return entityManager.createNamedQuery("stateByUuid", StateEntity.class).setParameter("uuid", stateUuid)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    /**
     * This method is to get all the states
     * @return all states name and uuid
     */
    public List<StateEntity> getAllStates() {
        try {
            return entityManager.createNamedQuery("allStates", StateEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

}