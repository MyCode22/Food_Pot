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


     Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("https://api.github.com/")
            .baseUrl("http://di-jual.com/mobileapp/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
