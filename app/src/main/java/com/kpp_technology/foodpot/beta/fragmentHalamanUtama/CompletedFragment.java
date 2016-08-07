package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapter;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;
import com.kpp_technology.foodpot.beta.itemObject.DataObject;

import java.util.ArrayList;


public class CompletedFragment extends Fragment {
    RecyclerView home_recycler_view;
    RecyclerView listFood;

    public CompletedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_progress, container, false);


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

            } catch (Exception er) {
                System.out.println("Error di MyService data " + er.getMessage());
            }

        }


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {
                DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
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


                        // System.out.println(r+" SET IMAGE LOGO " + logo[r]);

                        DataObject obj = new DataObject(merchant_id[r], merchant_name[r], address[r], ratings[r], votes[r],
                                is_open[r], logo[r], map_longitude[r], map_latitude[r]);
                        results.add(r, obj);
                        r++;


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
