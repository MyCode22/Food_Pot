package com.kpp_technology.foodpot.beta.itemObject;

/**
 * Created by Mobile-Tech on 10/15/2015.
 */

public class DataObject {
    private String merchant_id, merchant_name, address, ratings, votes, is_open, logo,
            map_longitude, map_latitude;


    public DataObject(String text1, String text3, String text4, String text5, String text8, String text13, String text17, String text18, String text19) {
        merchant_id = text1;
        merchant_name = text3;
        address = text4;
        ratings = text5;
        votes = text8;

        is_open = text13;

        logo = text17;
        map_longitude = text18;
        map_latitude = text19;

    }

    public String getMerchantId() {
        return merchant_id;
    }


    public String getMerchantName() {
        return merchant_name;
    }

    public String getAddress() {
        return address;
    }

    public String getRatings() {
        return ratings;
    }


    public String getVotes() {
        return votes;
    }


    public String getIsOpen() {
        return is_open;
    }


    public String getLogo() {
        return logo;
    }

    public String getMapLongitude() {
        return map_longitude;
    }

    public String getMapLatitude() {
        return map_latitude;
    }


}