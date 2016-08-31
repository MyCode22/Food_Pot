package com.kpp_technology.foodpot.beta.itemObject;

/**
 * Created by Mobile-Tech on 10/15/2015.
 */

public class DataObjectListMenu {
    private String cat_id, category_name, category_description, photo, status, sequence, date_created, date_modified,
            category_name_trans, category_description_trans, merchant_id;


    public DataObjectListMenu(String text1, String text3, String text4, String text5, String text8, String text13, String text17, String text18, String text19, String text20, String text21) {
        cat_id = text1;
        category_name = text3;
        category_description = text4;
        photo = text5;
        status = text8;
        sequence = text13;
        date_created = text17;
        date_modified = text18;
        category_name_trans = text19;
        category_description_trans = text20;
        merchant_id = text21;

    }

    public String getCatId() {
        return cat_id;
    }


    public String getCategoryName() {
        return category_name;
    }

    public String getCatDescription() {
        return category_description;
    }

    public String getPhoto() {
        return photo;
    }


    public String getStatus() {
        return status;
    }


    public String getSequence() {
        return sequence;
    }


    public String getDateCreated() {
        return date_created;
    }

    public String getDateModified() {
        return date_modified;
    }

    public String getCatNameTrans() {
        return category_name_trans;
    }

    public String getCatDescTrans() {
        return category_description_trans;
    }

    public String getMerchantId() {
        return merchant_id;
    }


}