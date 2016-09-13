package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.location.GPSTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsGetAddress extends AppCompatActivity {
    EditText editTextAddress;
    GoogleMap mMap;
    TextView NamaJalan;
    double latitude, longitude;
    Button buttonDOne;
    String addre, kota, provinsi, kodePOs, jalan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_maps_get_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        NamaJalan = (TextView) findViewById(R.id.NamaJalan);
        buttonDOne = (Button) findViewById(R.id.buttonDOne);
        GPSTracker gps = new GPSTracker(getApplicationContext());
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        editTextAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (editTextAddress.getInputType() == keyEvent.KEYCODE_ENTER) {
                    String ambil = getLonLat(editTextAddress.getText().toString());
                    System.out.println("Dapat lonltanyaaa " + ambil);
                }
                return false;
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.GetAddressMaps);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(latitude, longitude)).zoom(15).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        LatLng tes = cameraPosition.target;
                        latitude = tes.latitude;
                        longitude = tes.longitude;
                        addre = getAddress(tes.latitude, tes.longitude);
                        NamaJalan.setText(addre);
                    }
                });
            }
        });


        buttonDOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bundle b = new Bundle();
                    b.putString("status", "DONE");
                    b.putString("address", addre);
                    b.putString("longitude", String.valueOf(longitude));
                    b.putString("latitude", String.valueOf(latitude));
                    b.putString("jalan", jalan);
                    b.putString("kota", kota);
                    b.putString("provinsi", provinsi);
                    b.putString("kodePos", kodePOs);
                    Intent intent = new Intent();
                    intent.putExtras(b);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception er) {

                }
            }
        });


    }

    public String getAddress(Double latitude, Double longitude) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    jalan = address.getAddressLine(i).toString();
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                kota = address.getCountryName().toString();
                provinsi = address.getLocality().toString();
                kodePOs = address.getPostalCode().toString();
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                result = sb.toString();
            }
        } catch (IOException e) {
            // Log.e(TAG, "Unable connect to Geocoder", e);
            System.out.println("Unable connect to Geocorder " + e.getMessage());
        } finally {

            if (result != null) {

                Bundle bundle = new Bundle();
                result = "" + result;

            } else {

                Bundle bundle = new Bundle();
                result = "Unknown";

            }

        }
        return result;
    }


    public String getLonLat(String city) {
        String longi = null;
        String lati = null;
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocationName(city, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    longi = String.valueOf(address.getLongitude());
                    lati = String.valueOf(address.getLatitude());
                }


                result = lati + "||" + longi;
            }
        } catch (IOException e) {
            // Log.e(TAG, "Unable connect to Geocoder", e);
            System.out.println("Unable connect to Geocorder " + e.getMessage());
        }
        return result;
    }

}
