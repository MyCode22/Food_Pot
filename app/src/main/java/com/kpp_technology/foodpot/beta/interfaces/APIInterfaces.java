package com.kpp_technology.foodpot.beta.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Mobile Tech on 8/16/2016.
 */
public interface APIInterfaces {


    @FormUrlEncoded
    @POST("loadCart")
    Call<JsonObject> repoLoadCart(
            @Field("search_address") String search_address,
            @Field("cart") String cart,
            @Field("transaction_type") String transaction_type,
            @Field("lang_id") String lang_id,
            @Field("api_key") String api_key,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude

    );


    @FormUrlEncoded
    @POST("getEconomyDriver")
    Call<JsonObject> repoEconomyDriver(
            @Field("client_token") String client_token,
            @Field("cart") String cart,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude

    );


    @FormUrlEncoded
    @POST("placeOrder")
    Call<JsonObject> repoPlaceOrder(
            @Field("cart") String cart,
            @Field("transaction_type") String transaction_type,
            @Field("delivery_date") String delivery_date,
            @Field("delivery_time") String delivery_time,
            @Field("street") String street,
            @Field("city") String city,
            @Field("state") String state,
            @Field("zipcode") String zipcode,
            @Field("contact_phone") String contact_phone,
            @Field("location_name") String location_name,
            @Field("delivery_instruction") String delivery_instruction,
            @Field("client_token") String client_token,
            @Field("search_address") String search_address,
            @Field("payment_list") String payment_list,
            @Field("api_key") String api_key,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("driver_id") String driver_id

    );


    Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("https://api.github.com/")
            .baseUrl("http://di-jual.com/mobileapp/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
