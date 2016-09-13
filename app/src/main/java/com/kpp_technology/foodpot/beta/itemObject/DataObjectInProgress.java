package com.kpp_technology.foodpot.beta.itemObject;

/**
 * Created by Mobile Tech on 9/11/2016.
 */
public class DataObjectInProgress {
    String title, title2, status, id_order;

    public DataObjectInProgress(String title, String title2, String status, String id_order) {
        this.title = title;
        this.title2 = title2;
        this.status = status;
        this.id_order = id_order;
    }


    public String getTitle() {
        return title;
    }

    public String getTitle2() {
        return title2;
    }

    public String getStatus() {
        return status;
    }

    public String getId_order() {
        return id_order;
    }


}
