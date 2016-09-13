package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapterEconomyOrder;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;
import com.kpp_technology.foodpot.beta.interfaces.APIInterfaces;
import com.kpp_technology.foodpot.beta.itemObject.DataObjectEconomyOrder;
import com.kpp_technology.foodpot.beta.location.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EconomyOrder extends AppCompatActivity {
    public static RecyclerView listEconomyDriver;
    RecyclerView.LayoutManager mLayoutManager;
    String longitude, latitude, ItemArray, client_id, api_key;
    public static String[] driver_id, name_driver, region_driver, active_hour, pickupstandby;
    public static ArrayList results = new ArrayList<DataObjectEconomyOrder>();
    public static DataObjectEconomyOrder objectEconomyOrder;
    public static MyRecyclerViewAdapterEconomyOrder adapter;
    CheckBox check09, check12, check15, check17;
    LinearLayout linearOrderEconomy;
    public static String id_driver, jadwal_driver;

    String TimeKirim, dateKirim, weightSemuanya, jumlahHargaSemuanya, merchant_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_economy_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        try {
            GPSTracker gps = new GPSTracker(getApplicationContext());
            latitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
        } catch (Exception er) {

        }

        Intent ambil = getIntent();
        TimeKirim = ambil.getStringExtra("time");
        dateKirim = ambil.getStringExtra("date");
        weightSemuanya = ambil.getStringExtra("weight");
        jumlahHargaSemuanya = ambil.getStringExtra("harga");
        merchant_id = ambil.getStringExtra("merchant_id");


        final JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < MenuOrderActivity.hargaItem.size(); i++) {
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
        }


        ItemArray = jsonArray.toString();
        System.out.println("TESSSSSSSSS JSON ARRAYYYY \n " + jsonArray.toString());


        try {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.openDataBase();
            Cursor dat = db.getProfile();
            if (dat.moveToFirst()) {
                do {
                    client_id = dat.getString(dat.getColumnIndex("token"));
                    api_key = "ae5162a18e8afafd29b67b6ec2684813";
                    System.out.println("CLIENT ID " + client_id);
                    System.out.println("CLIENT ID " + api_key);
                } while (dat.moveToNext());
            }
            db.close();
        } catch (Exception er) {
            System.out.println("Errro di service " + er.getMessage());

        }


        check09 = (CheckBox) findViewById(R.id.check09);
        check12 = (CheckBox) findViewById(R.id.check12);
        check15 = (CheckBox) findViewById(R.id.check15);
        check17 = (CheckBox) findViewById(R.id.check17);
        linearOrderEconomy = (LinearLayout) findViewById(R.id.linearOrderEconomy);

        check09.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    jadwal_driver = "09:00";
                    check12.setChecked(false);
                    check15.setChecked(false);
                    check17.setChecked(false);
                }
            }
        });
        check12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    jadwal_driver = "12:00";
                    check09.setChecked(false);
                    check15.setChecked(false);
                    check17.setChecked(false);
                }
            }
        });
        check15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    jadwal_driver = "15:00";
                    check12.setChecked(false);
                    check09.setChecked(false);
                    check17.setChecked(false);
                }
            }
        });
        check17.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    jadwal_driver = "17:00";
                    check12.setChecked(false);
                    check15.setChecked(false);
                    check09.setChecked(false);
                }
            }
        });


        listEconomyDriver = (RecyclerView) findViewById(R.id.listEconomyDriver);

        listEconomyDriver.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(EconomyOrder.this);
        listEconomyDriver.setLayoutManager(mLayoutManager);


        linearOrderEconomy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pindah = new Intent(EconomyOrder.this, CheckoutPayment.class);
                pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pindah.putExtra("time", jadwal_driver);
                pindah.putExtra("date", dateKirim);
                pindah.putExtra("weight", String.valueOf(weightSemuanya));
                pindah.putExtra("harga", String.valueOf(jumlahHargaSemuanya));
                pindah.putExtra("merchant_id", merchant_id);
                pindah.putExtra("type_transaksi", "economy");
                pindah.putExtra("getDriverId", id_driver);
                pindah.putExtra("jadwal_driver", jadwal_driver);
                startActivity(pindah);
            }
        });


        getDriverEconomy();

    }

    private void getDriverEconomy() {

        try {
            APIInterfaces gitHubService = APIInterfaces.retrofit.create(APIInterfaces.class);
            final Call<JsonObject> call = gitHubService.repoEconomyDriver(client_id, ItemArray, latitude, longitude);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonobj = new JSONObject(response.body().toString());
                        String msg = jsonobj.getString("msg");
                        if (msg.equals("OK")) {
                            JSONArray arry = jsonobj.getJSONArray("details");
                            int jum = arry.length();
                            driver_id = new String[jum];
                            name_driver = new String[jum];
                            region_driver = new String[jum];
                            active_hour = new String[jum];
                            pickupstandby = new String[jum];

                            for (int r = 0; r < jum; r++) {
                                JSONObject dat = arry.getJSONObject(r);
                                driver_id[r] = dat.getString("driver_id");
                                name_driver[r] = dat.getString("name");
                                region_driver[r] = dat.getString("region");
                                pickupstandby[r] = dat.getString("pickupstandby");
                                active_hour[r] = dat.getString("active_hour").toString();

                                objectEconomyOrder = new DataObjectEconomyOrder(driver_id[r], name_driver[r], region_driver[r], pickupstandby[r], active_hour[r], "kosong");
                                results.add(r, objectEconomyOrder);
                            }

                            adapter = new MyRecyclerViewAdapterEconomyOrder(getApplicationContext(), results);
                            listEconomyDriver.setAdapter(adapter);


                        }


                    } catch (Exception er) {

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (Exception er) {

        }
    }

    public static void changeBagroun(int position, Context context) {


        results.clear();
        for (int r = 0; r < driver_id.length; r++) {

            if (r == position) {
                id_driver = driver_id[r];
                objectEconomyOrder = new DataObjectEconomyOrder(driver_id[r], name_driver[r], region_driver[r], pickupstandby[r], active_hour[r], "orange");
            } else {
                objectEconomyOrder = new DataObjectEconomyOrder(driver_id[r], name_driver[r], region_driver[r], pickupstandby[r], active_hour[r], "kosong");
            }

            results.add(r, objectEconomyOrder);
        }

        adapter = new MyRecyclerViewAdapterEconomyOrder(context, results);
        listEconomyDriver.setAdapter(adapter);

       /*
        objectEconomyOrder = new DataObjectEconomyOrder(driver_id[position], name_driver[position], region_driver[position], pickupstandby[position], active_hour[position], "orange");
        adapter.updateItem(position);*/
    }


}
