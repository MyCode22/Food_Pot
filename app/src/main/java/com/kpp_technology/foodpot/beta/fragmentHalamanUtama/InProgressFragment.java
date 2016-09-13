package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapter;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapterInProgress;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;
import com.kpp_technology.foodpot.beta.itemObject.DataObjectInProgress;

import java.util.ArrayList;


public class InProgressFragment extends Fragment {
    MyRecyclerViewAdapterInProgress adapter;
    RecyclerView listInProgress;
    DataObjectInProgress object;
    ArrayList results = new ArrayList<DataObjectInProgress>();
    RecyclerView.LayoutManager mLayoutManager;

    public InProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_progress, container, false);
        System.out.println("Masuk ke INPROGRESSS");
        listInProgress = (RecyclerView) view.findViewById(R.id.listInProgress);
        listInProgress.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listInProgress.setLayoutManager(mLayoutManager);


        new getHistoryOrder().execute();


        return view;
    }

    public class getHistoryOrder extends AsyncTask<String, Void, String> {

        int r = 0;

        MyRecyclerViewAdapter mAdapter;
        String[] order_id, title, title2, status;

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
                Cursor data = db.getHistoryOrder("pending");
                System.out.println("JUMLAH DATA PENDING " + data.getCount());
                order_id = new String[data.getCount()];
                title = new String[data.getCount()];
                title2 = new String[data.getCount()];
                status = new String[data.getCount()];

                results.clear();
                if (data.moveToFirst()) {
                    do {

                        order_id[r] = data.getString(data.getColumnIndex("order_id"));
                        title[r] = data.getString(data.getColumnIndex("title"));
                        title2[r] = data.getString(data.getColumnIndex("title2"));
                        status[r] = data.getString(data.getColumnIndex("status"));


                        // System.out.println(r+" SET IMAGE LOGO " + logo[r]);

                        object = new DataObjectInProgress(title[r], title2[r], status[r], order_id[r]);

                        results.add(r, object);

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
                adapter = new MyRecyclerViewAdapterInProgress(getActivity().getApplicationContext(), results);
                listInProgress.setAdapter(adapter);
               /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, title);
                listInProgress.setAdapter(adapter);*/

            } catch (Exception er) {

            }

        }

        @Override
        protected void onCancelled() {

        }
    }

}
