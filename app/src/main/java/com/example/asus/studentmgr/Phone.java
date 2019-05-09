package com.example.asus.studentmgr;

import java.io.Serializable;

/**
 * Created by asus on 2019/4/9.
 */

public class Phone implements Serializable{
    private String phoneNumber;
    private String placeProvince;
    private String placeArea;
    private String carrier;
    private String placeCarrier;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    public String getPlaceProvince() {
        return placeProvince;
    }

    public void setPlaceProvince(String province) {
        this.placeProvince = province;
    }

    public String getPlaceArea(){ return placeArea; }

    public void setPlaceArea(String area){ this.placeArea=area; }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getPlaceCarrier() {
        return placeCarrier;
    }

    public void setPlaceCarrier(String ownerCarrier) {
        this.placeCarrier = ownerCarrier;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "number='" + phoneNumber + '\'' +
                ", province='" + placeProvince + '\'' +
                ",area='"+placeArea+'\''+
                ", carrier='" + carrier + '\'' +
                ", ownerCarrier='" + placeCarrier + '\'' +
                '}';
    }
}
