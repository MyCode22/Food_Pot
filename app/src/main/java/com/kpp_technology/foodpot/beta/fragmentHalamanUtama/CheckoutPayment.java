package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;
import com.kpp_technology.foodpot.beta.interfaces.APIInterfaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutPayment extends AppCompatActivity {
    LinearLayout linearback;
    String getTime, getDate, getWeight, getHarga, getMerchantId, address, longitudeAddress, latitudeAddress, ItemArray;
    TextView weightBooking, jumlahHargaBooking, Textaddres, textPickUpTime, textCash, TexttotalPrice, textTax, textDeliveryFee;
    RadioGroup myPayment;
    LinearLayout testter, pilihDeliver, linearOrderFinish;
    String merchant_longitude, merchant_latitude, token;
    double distance, hargaTax, hargaMakanan, hargaFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent ambil = getIntent();
        getTime = ambil.getStringExtra("time");
        getDate = ambil.getStringExtra("date");
        getWeight = ambil.getStringExtra("weight");
        getHarga = ambil.getStringExtra("harga");
        getMerchantId = ambil.getStringExtra("merchant_id");


        linearback = (LinearLayout) findViewById(R.id.linearback);
        linearOrderFinish = (LinearLayout) findViewById(R.id.linearOrderFinish);
        weightBooking = (TextView) findViewById(R.id.weightBooking);
        jumlahHargaBooking = (TextView) findViewById(R.id.jumlahHargaBooking);
        Textaddres = (TextView) findViewById(R.id.Textaddres);
        textPickUpTime = (TextView) findViewById(R.id.textPickUpTime);
        TexttotalPrice = (TextView) findViewById(R.id.TexttotalPrice);
        textTax = (TextView) findViewById(R.id.textTax);
        textCash = (TextView) findViewById(R.id.textCash);
        textDeliveryFee = (TextView) findViewById(R.id.textDeliveryFee);
        testter = (LinearLayout) findViewById(R.id.testter);
        pilihDeliver = (LinearLayout) findViewById(R.id.pilihDeliver);
        myPayment = (RadioGroup) findViewById(R.id.myPayment);


        try {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.openDataBase();
            Cursor data = db.getAllMerchantById(getMerchantId);
            if (data.moveToFirst()) {
                do {
                    merchant_latitude = data.getString(data.getColumnIndex("map_latitude"));
                    merchant_longitude = data.getString(data.getColumnIndex("map_longitude"));

                    System.out.println(getMerchantId + " <<< merchant_latitude >>> " + merchant_latitude);
                    System.out.println(getMerchantId + "<<<<< merchant_longitude >>>> " + merchant_longitude);

                } while (data.moveToNext());
            }

            Cursor dataProf = db.getProfile();
            if (dataProf.moveToFirst()) {
                do {
                    String sal = dataProf.getString(data.getColumnIndex("saldo"));

                    textCash.setText("RP. " + sal);

                } while (dataProf.moveToNext());
            }


            db.close();
        } catch (Exception er) {

        }


        myPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.cash) {

                } else if (checkedId == R.id.credit) {
                }
            }
        });

        weightBooking.setText(getWeight + " Kg");
        jumlahHargaBooking.setText("Rp. " + getHarga);
        textPickUpTime.setText(getDate + " " + getTime);

        try {
            hargaMakanan = Double.valueOf(getHarga);
            hargaTax = 5 * Double.valueOf(getHarga) / 100;
            textTax.setText("Rp." + hargaTax);
        } catch (Exception er) {

        }


        testter.removeAllViews();
        final JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < MenuOrderActivity.hargaItem.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View ll = inflater.inflate(R.layout.list_booking_confirmation, null);

            TextView name = (TextView) ll.findViewById(R.id.nameItemBooking);
            TextView priceItemBooking = (TextView) ll.findViewById(R.id.priceItemBooking);
            TextView jumlahItemBooking = (TextView) ll.findViewById(R.id.jumlahItemBooking);

            name.setText(MenuOrderActivity.nameItem.get(i));
            priceItemBooking.setText("@ " + MenuOrderActivity.hargaItem.get(i));
            jumlahItemBooking.setText("" + MenuOrderActivity.jumlahItem.get(i));

            JSONObject student2 = new JSONObject();
            try {
                student2.put("item_id", MenuOrderActivity.idItem.get(i));
                student2.put("qty", MenuOrderActivity.jumlahItem.get(i));
                student2.put("order_notes", "");
                jsonArray.put(student2);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Errorrr json Array " + e.getMessage());
            }


            testter.addView(ll);
        }

        JSONObject studentsObj = new JSONObject();
        try {
            studentsObj.put("Students", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ItemArray = jsonArray.toString();
        System.out.println("TESSSSSSSSS JSON ARRAYYYY \n " + jsonArray.toString());
        System.out.println("TESSSSSSSSS JSON ARRAYYYY \n " + studentsObj.toString());


        pilihDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent pindah = new Intent(CheckoutPayment.this, MapsGetAddress.class);
                    startActivityForResult(pindah, 123);
                } catch (Exception er) {
                    System.out.println("Error karena get Address " + er.getMessage());
                }
            }
        });


        linearOrderFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String add = Textaddres.getText().toString();

                    System.out.println("add >> " + add);
                    System.out.println("express >> express");
                    System.out.println("id_lang >> 1");
                    System.out.println("add >> ae5162a18e8afafd29b67b6ec2684813");
                    System.out.println("longitudeAddress >> " + longitudeAddress);
                    System.out.println("latitudeAddress >> " + latitudeAddress);

                    APIInterfaces gitHubService = APIInterfaces.retrofit.create(APIInterfaces.class);
                    final Call<JsonObject> call = gitHubService.repoLoadCart(add, ItemArray, "express", "1", "ae5162a18e8afafd29b67b6ec2684813", longitudeAddress, latitudeAddress);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            System.out.println("HASIL LOAD CAST " + response.body().toString());
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            System.out.println("GAGALA " + t.getMessage());

                        }
                    });
                } catch (Exception er) {

                }
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 123:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String status = bundle.getString("status");
                    address = bundle.getString("address");
                    longitudeAddress = bundle.getString("longitude");
                    latitudeAddress = bundle.getString("latitude");
                    Textaddres.setText(address);

                    System.out.println("longitudeAddress " + longitudeAddress);
                    System.out.println("latitudeAddress " + latitudeAddress);
                    try {

                        distance = DistanceBetweenPointsinKM(Double.valueOf(latitudeAddress), Double.valueOf(longitudeAddress), Double.valueOf(merchant_latitude), Double.valueOf(merchant_longitude));

                        hargaFee = 5000 * distance;

                        textDeliveryFee.setText(" Rp." + hargaFee);

                        double hargaSemuanya = hargaFee + hargaMakanan + hargaTax;
                        TexttotalPrice.setText("Rp." + hargaSemuanya);

                    } catch (Exception er) {
                        System.out.println("Error get distance " + er.getMessage());
                    }

                }
                break;
        }

    }


    private double DistanceBetweenPointsinKM(double latitude, double longitude, double latitudeB, double longitudeB) {
        Location locationA = new Location("point A");
        locationA.setLatitude(latitude);
        locationA.setLongitude(longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latitudeB);
        locationB.setLongitude(longitudeB);
        double distance = locationA.distanceTo(locationB) / 1000;

        return distance;
    }


}
