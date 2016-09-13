package com.kpp_technology.foodpot.beta.itemObject;

/**
 * Created by Mobile Tech on 9/11/2016.
 */
public class DataObjectEconomyOrder {

    String id_driver;
    String name_driver;
    String regio_driver;
    String pickupstandby;
    String active_hour;


    String pilih;


    public String getId_driver() {
        return id_driver;
    }

    public String getName_driver() {
        return name_driver;
    }

    public String getRegio_driver() {
        return regio_driver;
    }

    public String getPickupstandby() {
        return pickupstandby;
    }

    public String getActive_hour() {
        return active_hour;
    }

    public String getPilih() {
        return pilih;
    }

    public DataObjectEconomyOrder(String id_driver, String name_driver, String regio_driver, String pickupstandby, String active_hour, String pilih) {
        this.id_driver = id_driver;
        this.name_driver = name_driver;
        this.regio_driver = regio_driver;
        this.pickupstandby = pickupstandby;
        this.active_hour = active_hour;
        this.pilih = pilih;
    }


}
