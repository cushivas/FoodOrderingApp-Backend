package com.upgrad.FoodOrderingApp.service.entity;
/**
 * @author soniya kocher
 *
 * This class is Address entity class
 */

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Entity
@Table(name = "address")
@NamedQueries(
        {

                @NamedQuery(name = "addressByUuid", query = "select a from AddressEntity a where a.uuid =:uuid")

        }
)

public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;


    @Column(name = "flat_buil_number")
    @Size(max = 255)
    private String flatBuildingNumber;

    @Column(name = "locality")
    @Size(max = 255)
    private String locality;

    @Column(name = "city")
    @Size(max = 30)
    private String city;

    @Column(name = "pincode")

    @Size(max = 30)
    private String pincode;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
     @JoinColumn(name = "state_id")
  private StateEntity state;

    @Column(name = "active")
    private Integer active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatBuildingNumber() {
        return flatBuildingNumber;
    }

    public void setFlatBuildingNumber(String flatBuildingNumber) {
        this.flatBuildingNumber = flatBuildingNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public AddressEntity(){}

    public AddressEntity(String uuid, String flatBuilNo, String locality, String city, String pincode, StateEntity stateEntity) {
        this.uuid = uuid;
        this.flatBuildingNumber =flatBuilNo;
        this.locality = locality;
        this.city = city;
        this.pincode = pincode;
        this.state = stateEntity;
        return;
    }



}

