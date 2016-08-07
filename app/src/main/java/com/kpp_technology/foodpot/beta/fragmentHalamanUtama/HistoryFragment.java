package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapter;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;
import com.kpp_technology.foodpot.beta.itemObject.DataObject;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    RecyclerView listFood;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_history);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs_history);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new InProgressFragment(), "IN PROGRESS");
        adapter.addFragment(new CompletedFragment(), "COMPLETED");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
