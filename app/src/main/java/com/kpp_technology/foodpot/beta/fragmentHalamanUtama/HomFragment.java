package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapter;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;
import com.kpp_technology.foodpot.beta.itemObject.DataObject;

import java.util.ArrayList;


public class HomFragment extends Fragment {
    RecyclerView home_recycler_view;
    RecyclerView listFood;
    LinearLayout linearNearMe;

    public HomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listFood = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        linearNearMe = (LinearLayout) view.findViewById(R.id.linearNearMe);

        listFood.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listFood.setLayoutManager(mLayoutManager);

        new getMerchant().execute();


        linearNearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    System.out.println("klik linear me");
                    Intent pindah = new Intent(getActivity(), NearMeActivity.class);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pindah);
                } catch (Exception er) {
                    System.out.println("Error klik " + er.getMessage());
                }
            }
        });


        return view;
    }

    public class getMerchant extends AsyncTask<String, Void, String> {
        String[] merchant_id, merchant_name, address, ratings, votes, is_open, logo, map_longitude, map_latitude;
        int r = 0;
        ArrayList results = new ArrayList<DataObject>();
        MyRecyclerViewAdapter mAdapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                System.out.println("Masuk ek da;am list merchant ");
            } catch (Exception er) {
                System.out.println("Error di MyService data " + er.getMessage());
            }

        }


        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("Mulai queryyy ke DB");
                DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
                db.openDataBase();
                Cursor data = db.getAllMerchant();
                System.out.println("Hasil querryyy " + data.getCount());
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


                        System.out.println(r + " SET IMAGE LOGO " + logo[r]);
                        System.out.println(r + " NAME MERCHANT " + merchant_name[r]);

                        DataObject obj = new DataObject(merchant_id[r], merchant_name[r], address[r], ratings[r], votes[r],
                                is_open[r], logo[r], map_longitude[r], map_latitude[r]);
                        results.add(r, obj);
                        r++;


                    } while (data.moveToNext());
                }

                db.close();
            } catch (Exception e) {
                System.out.println("Errorrr get merchant " + e.getMessage());
                return null;
            }


            // TODO: register the new account here.
            return null;
        }

        @Override
        protected void onPostExecute(String hasil) {
            try {

                mAdapter = new MyRecyclerViewAdapter(getActivity().getApplicationContext(), results);
                listFood.setAdapter(mAdapter);

            } catch (Exception er) {

            }

        }

        @Override
        protected void onCancelled() {

        }
    }

}
