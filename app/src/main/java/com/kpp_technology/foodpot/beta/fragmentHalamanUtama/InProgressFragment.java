package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapter;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;


public class InProgressFragment extends Fragment {
    RecyclerView home_recycler_view;
    ListView listInProgress;

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
        listInProgress = (ListView) view.findViewById(R.id.listInProgress);

        return view;
    }

    public class getHistoryOrder extends AsyncTask<String, Void, String> {

        int r = 0;

        MyRecyclerViewAdapter mAdapter;
        String[] order_id, title, status;

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
                order_id = new String[data.getCount()];
                title = new String[data.getCount()];
                status = new String[data.getCount()];


                if (data.moveToFirst()) {
                    do {

                        order_id[r] = data.getString(data.getColumnIndex("order_id"));
                        title[r] = data.getString(data.getColumnIndex("title"));
                        status[r] = data.getString(data.getColumnIndex("status"));


                        // System.out.println(r+" SET IMAGE LOGO " + logo[r]);


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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, title);
                listInProgress.setAdapter(adapter);

            } catch (Exception er) {

            }

        }

        @Override
        protected void onCancelled() {

        }
    }

}
