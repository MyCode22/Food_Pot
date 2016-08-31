package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpp_technology.foodpot.beta.R;

public class CheckoutPayment extends AppCompatActivity {
    LinearLayout linearback;
    String getTime, getDate, getWeight, getHarga;
    TextView weightBooking, jumlahHargaBooking;

    LinearLayout testter, pilihDeliver;

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

        linearback = (LinearLayout) findViewById(R.id.linearback);
        weightBooking = (TextView) findViewById(R.id.weightBooking);
        jumlahHargaBooking = (TextView) findViewById(R.id.jumlahHargaBooking);
        testter = (LinearLayout) findViewById(R.id.testter);
        pilihDeliver = (LinearLayout) findViewById(R.id.pilihDeliver);

        weightBooking.setText(getWeight + " Kg");
        jumlahHargaBooking.setText("Rp. " + getHarga);


        testter.removeAllViews();
        for (int i = 0; i < MenuOrderActivity.hargaItem.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View ll = inflater.inflate(R.layout.list_booking_confirmation, null);

            TextView name = (TextView) ll.findViewById(R.id.nameItemBooking);
            TextView priceItemBooking = (TextView) ll.findViewById(R.id.priceItemBooking);
            TextView jumlahItemBooking = (TextView) ll.findViewById(R.id.jumlahItemBooking);

            name.setText(MenuOrderActivity.nameItem.get(i));
            priceItemBooking.setText("@ " + MenuOrderActivity.hargaItem.get(i));
            jumlahItemBooking.setText("" + MenuOrderActivity.jumlahItem.get(i));

            testter.addView(ll);
        }


        pilihDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

}
