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
import com.kpp_technology.foodpot.beta.BerandaActivity;
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
    String kota, provinsi, kodePOs, jalan;
    String getTime, getDate, getWeight, getHarga, getMerchantId, getDriverId,getJadwalDriver, address, longitudeAddress, latitudeAddress, ItemArray, getTypeTransaksi;
    TextView weightBooking, jumlahHargaBooking, Textaddres, textPickUpTime, textCash, TexttotalPrice, textTax, textDeliveryFee;
    RadioGroup myPayment;
    LinearLayout testter, pilihDeliver, linearOrderFinish;
    String merchant_longitude, merchant_latitude, token;
    double distance, hargaTax, hargaMakanan, hargaFee;
    String noTelpProfile, client_token, type_payment = "cod";

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
        getTypeTransaksi = ambil.getStringExtra("type_transaksi");
        getDriverId = ambil.getStringExtra("getDriverId");
        getJadwalDriver = ambil.getStringExtra("jadwal_driver");

        try {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.openDataBase();
            Cursor data = db.getProfile();
            if (data.moveToFirst()) {
                do {
                    noTelpProfile = data.getString(data.getColumnIndex("contact_phone"));
                    client_token = data.getString(data.getColumnIndex("client_token"));

                } while (data.moveToNext());
            }
            db.close();
        } catch (Exception er) {

        }


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
                    type_payment = "cod";
                } else if (checkedId == R.id.credit) {
                    type_payment = "credit";
                }
            }
        });

        weightBooking.setText(getWeight + " Kg");
        jumlahHargaBooking.setText("Rp. " + getHarga);
        textPickUpTime.setText(getDate + " " + getTime);

        try {
            hargaMakanan = Double.valueOf(getHarga);
            hargaTax = 5 * Double.valueOf(getHarga) / 100;
            //  textTax.setText("Rp." + hargaTax);
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
                    String apikey = getResources().getString(R.string.apikey);
                    APIInterfaces gitHubService = APIInterfaces.retrofit.create(APIInterfaces.class);

                    final Call<JsonObject> call = gitHubService.repoPlaceOrder(ItemArray, getTypeTransaksi, getDate, getTime, jalan, kota, provinsi, kodePOs, noTelpProfile, "", "", client_token, add, type_payment, apikey, latitudeAddress, longitudeAddress, getDriverId);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            System.out.println("Response send place order " + response.body().toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                String msg = jsonObject.getString("code");
                                if (msg.equals("1")) {
                                    Intent pindah = new Intent(CheckoutPayment.this, BerandaActivity.class);
                                    pindah.putExtra("status", "orderFinish");
                                    pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(pindah);
                                }
                            } catch (Exception er) {

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            System.out.println("Errorr place order " + t.getMessage());
                        }
                    });
                } catch (Exception er) {
                    System.out.println("Errorrr sent " + er.getMessage());
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
                    kodePOs = bundle.getString("kodePOs");
                    jalan = bundle.getString("jalan");
                    provinsi = bundle.getString("provinsi");
                    kota = bundle.getString("kota");
                    Textaddres.setText(address);

          /*          System.out.println("longitudeAddress " + longitudeAddress);
                    System.out.println("latitudeAddress " + latitudeAddress);
                    try {

                        distance = DistanceBetweenPointsinKM(Double.valueOf(latitudeAddress), Double.valueOf(longitudeAddress), Double.valueOf(merchant_latitude), Double.valueOf(merchant_longitude));

                        hargaFee = 5000 * distance;

                        textDeliveryFee.setText(" Rp." + hargaFee);

                        double hargaSemuanya = hargaFee + hargaMakanan + hargaTax;
                        TexttotalPrice.setText("Rp." + hargaSemuanya);

                    } catch (Exception er) {
                        System.out.println("Error get distance " + er.getMessage());
                    }*/

                    sentLoadCart();


                }
                break;
        }

    }

    private void sentLoadCart() {
        try {
            String add = Textaddres.getText().toString();

           /* System.out.println("add >> " + add);
            System.out.println("express >> express");
            System.out.println("id_lang >> 1");
            System.out.println("add >> ae5162a18e8afafd29b67b6ec2684813");
            System.out.println("longitudeAddress >> " + longitudeAddress);
            System.out.println("latitudeAddress >> " + latitudeAddress);*/

            APIInterfaces gitHubService = APIInterfaces.retrofit.create(APIInterfaces.class);
            final Call<JsonObject> call = gitHubService.repoLoadCart(add, ItemArray, getTypeTransaksi, "1", "ae5162a18e8afafd29b67b6ec2684813", longitudeAddress, latitudeAddress);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    System.out.println("HASIL LOAD CAST " + response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String msg = jsonObject.getString("msg");
                        if (msg.equals("OK")) {
                            String tax_currency = jsonObject.getJSONObject("details").getJSONObject("cart").getString("tax_currency");
                            String delivery_charge_currency = jsonObject.getJSONObject("details").getJSONObject("cart").getString("delivery_charge_currency");
                            String sub_total_currency = jsonObject.getJSONObject("details").getJSONObject("cart").getString("sub_total_currency");
                            String total_currency = jsonObject.getJSONObject("details").getJSONObject("cart").getString("total_currency");


                            textTax.setText(tax_currency);
                            textDeliveryFee.setText(delivery_charge_currency);
                            TexttotalPrice.setText(total_currency);
                            jumlahHargaBooking.setText(sub_total_currency);
                        }


                    } catch (Exception er) {
                        System.out.println("Errro laod chary " + er.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println("gagal load chart " + t.getMessage());

                }
            });
        } catch (Exception er) {
            System.out.println("Errorrrrr " + er.getMessage());
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
