package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapter;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;
import com.kpp_technology.foodpot.beta.itemObject.DataObject;

import java.util.ArrayList;

public class NearMeActivity extends AppCompatActivity {
    LinearLayout linearback;
    private GoogleMap mMap;
    RecyclerView listFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_near_me);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        linearback = (LinearLayout) findViewById(R.id.linearback);
        listFood = (RecyclerView) findViewById(R.id.near_recycler_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapNearMe);


        listFood.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NearMeActivity.this);
        listFood.setLayoutManager(mLayoutManager);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                new getMerchant().execute();
            }
        });


        linearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onBackPressed();
                } catch (Exception er) {

                }
            }
        });

    }

/*    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // new getMerchant().execute();


    }*/


    public class getMerchant extends AsyncTask<String, Void, String> {
        String[] merchant_id, merchant_name, address, ratings, votes, is_open, logo, map_longitude, map_latitude;
        int r = 0;
        ArrayList results = new ArrayList<DataObject>();
        MyRecyclerViewAdapter mAdapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

            } catch (Exception er) {
                System.out.println("Error di MyService data " + er.getMessage());
            }

        }


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                db.openDataBase();
                Cursor data = db.getAllMerchant();
                merchant_id = new String[data.getCount()];
                merchant_name = new String[data.getCount()];
                address = new String[data.getCount()];
                ratings = new String[data.getCount()];
                votes = new String[data.getCount()];

                is_open = new String[data.getCount()];

                logo = new String[data.getCount()];
                map_longitude = new String[data.getCount()];
                map_latitude = new String[data.getCount()];


                if (data.moveToFirst()) {
                    do {

                        merchant_id[r] = data.getString(data.getColumnIndex("merchant_id"));
                        merchant_name[r] = data.getString(data.getColumnIndex("merchant_name"));
                        address[r] = data.getString(data.getColumnIndex("address"));
                        ratings[r] = data.getString(data.getColumnIndex("ratings"));
                        votes[r] = data.getString(data.getColumnIndex("votes"));
                        is_open[r] = data.getString(data.getColumnIndex("is_open"));
                        logo[r] = data.getString(data.getColumnIndex("logo"));
                        map_longitude[r] = data.getString(data.getColumnIndex("map_longitude"));
                        map_latitude[r] = data.getString(data.getColumnIndex("map_latitude"));


                        DataObject obj = new DataObject(merchant_id[r], merchant_name[r], address[r], ratings[r], votes[r],
                                is_open[r], logo[r], map_longitude[r], map_latitude[r]);
                        results.add(r, obj);
                        r++;

                        try {
                            double longitude = mMap.getMyLocation().getLongitude();
                            double latitude = mMap.getMyLocation().getLatitude();
                            double longitudeB = Double.valueOf(map_longitude[r]);
                            double latitudeB = Double.valueOf(map_latitude[r]);

                            float dis = meterDistanceBetweenPoints(latitude, longitude, latitudeB, longitudeB);

                            db.updateDistanceMerchant(merchant_id[r], String.valueOf(dis));

                        } catch (Exception er) {

                        }


                    } while (data.moveToNext());
                }

                db.close();
            } catch (Exception e) {
                return null;
            }


            // TODO: register the new account here.
            return null;
        }

        @Override
        protected void onPostExecute(String hasil) {
            try {


                for (int e = 0; e < map_latitude.length; e++) {
                    try {
                        System.out.println("longitude " + map_longitude[e]);
                        System.out.println("longitude " + map_latitude[e]);
                        LatLng sydney = new LatLng(Double.valueOf(map_latitude[e]), Double.valueOf(map_longitude[e]));
                        mMap.addMarker(new MarkerOptions().position(sydney).title(merchant_name[e]));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                new LatLng(Double.valueOf(map_latitude[e]), Double.valueOf(map_longitude[e]))).zoom(12).build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    } catch (Exception er) {
                        System.out.println("Errorrrr add marker " + er.getMessage());
                    }
                }


                mAdapter = new MyRecyclerViewAdapter(getApplicationContext(), results);
                listFood.setAdapter(mAdapter);

            } catch (Exception er) {

            }

        }

        @Override
        protected void onCancelled() {

        }
    }

    private float meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {

        Location locationA = new Location("point A");
        locationA.setLatitude(lat_a);
        locationA.setLongitude(lng_a);
        Location locationB = new Location("point B");
        locationB.setLatitude(lat_b);
        locationB.setLongitude(lng_b);
        float distance = locationA.distanceTo(locationB);

        return distance;
    }
}
