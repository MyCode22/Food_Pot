package com.kpp_technology.foodpot.beta;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.kpp_technology.foodpot.beta.fragmentHalamanUtama.HistoryFragment;
import com.kpp_technology.foodpot.beta.fragmentHalamanUtama.HomFragment;
import com.kpp_technology.foodpot.beta.fragmentHalamanUtama.SettingFragment;
import com.kpp_technology.foodpot.beta.fragmentLogin.SignUpFragment;

import java.util.ArrayList;
import java.util.List;

public class BerandaActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ProgressDialog pDialog;
    Runnable waktu = null;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_beranda);

        viewPager = (ViewPager) findViewById(R.id.viewpagerBeranda);
        tabLayout = (TabLayout) findViewById(R.id.tabsBeranda);

        pDialog = new ProgressDialog(BerandaActivity.this);
        pDialog.setMessage("Get Profile Login...");
        pDialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (MyService.statusMerchantList.equals("No")) {
            pDialog.show();
        }


        waktu = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(waktu, 5000);
                System.out.println(">>>> " + MyService.statusMerchantList);
                if (MyService.statusMerchantList.equals("Yes")) {
                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                    setupTabIcons();
                    pDialog.dismiss();
                    handler.removeCallbacks(waktu);
                }

            }
        };
        handler.post(waktu);


    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.home_active,
                R.drawable.history,
                R.drawable.help,
                R.drawable.settings
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomFragment(), "");
        adapter.addFragment(new HistoryFragment(), "");
        adapter.addFragment(new SignUpFragment(), "");
        adapter.addFragment(new SettingFragment(), "");

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
}
