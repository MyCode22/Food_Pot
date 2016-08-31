package com.kpp_technology.foodpot.beta.itemObject;

/**
 * Created by Mobile-Tech on 10/15/2015.
 */

public class DataObjectListMenuOrder {
    private String category, item_id, item_name, item_description, discount, weight, uom, stock, views, sold, min_order,
            sequence, is_featured, photo, not_available, retail_price, currency_retail_price, merchant_id;


    public DataObjectListMenuOrder(String text1, String text2, String text3, String text4, String text5,
                                   String text6, String text7, String text8, String text9, String text10,
                                   String text11, String text12, String text13, String text14, String text15,
                                   String text16, String text17, String text18) {
        category = text1;
        item_id = text2;
        item_name = text3;
        item_description = text4;
        discount = text5;
        weight = text6;
        uom = text7;
        stock = text8;
        views = text9;
        sold = text10;
        min_order = text11;
        sequence = text12;
        is_featured = text13;
        photo = text14;
        not_available = text15;
        retail_price = text16;
        currency_retail_price = text17;
        merchant_id = text18;

    }

    public String getCategory() {
        return category;
    }


    public String getItemId() {
        return item_id;
    }

    public String getItemName() {
        return item_name;
    }

    public String getPhoto() {
        return photo;
    }


    public String getItemDesc() {
        return item_description;
    }


    public String getSequence() {
        return sequence;
    }


    public String getDiscount() {
        return discount;
    }

    public String getWeight() {
        return weight;
    }

    public String getUom() {
        return uom;
    }

    public String getStock() {
        return stock;
    }

    public String getViews() {
        return views;
    }

    public String getSold() {
        return sold;
    }

    public String getMinOrder() {
        return min_order;
    }

    public String getIsFeatured() {
        return is_featured;
    }

    public String getNotAvaible() {
        return not_available;
    }

    public String getRetailPrice() {
        return retail_price;
    }

    public String getCurencyRetail() {
        return currency_retail_price;
    }


    public String getMerchantId() {
        return merchant_id;
    }


}