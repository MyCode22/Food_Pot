package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapterListMenu;
import com.kpp_technology.foodpot.beta.database.DatabaseHelper;
import com.kpp_technology.foodpot.beta.itemObject.DataObjectListMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListMenuRestActivity extends Activity {
    String merchant_id;
    RecyclerView listMenuCategory;
    ImageView imageMerchant;
    TextView namaMerchant, alamatMerchant, jarakMerchant, alamatMerchant2, businesHour;
    LinearLayout linearCall, linearMaps;
    MyRecyclerViewAdapterListMenu mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_list_menu_rest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        Intent ambil = getIntent();
        merchant_id = ambil.getStringExtra("merchant_id");

        listMenuCategory = (RecyclerView) findViewById(R.id.listMenuCategory);
        imageMerchant = (ImageView) findViewById(R.id.imageMerchant);
        namaMerchant = (TextView) findViewById(R.id.namaMerchant);
        alamatMerchant = (TextView) findViewById(R.id.alamatMerchant);
        alamatMerchant2 = (TextView) findViewById(R.id.alamatMerchant2);
        jarakMerchant = (TextView) findViewById(R.id.jarakMerchant);
        businesHour = (TextView) findViewById(R.id.businesHour);
        linearCall = (LinearLayout) findViewById(R.id.linearCall);
        linearMaps = (LinearLayout) findViewById(R.id.linearMaps);


        try {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.openDataBase();
            Cursor data = db.getAllMerchantById(merchant_id);
            if (data.moveToFirst()) {
                do {

                    Bitmap bmp = BitmapFactory.decodeFile(data.getString(data.getColumnIndex("logo")));
                    imageMerchant.setImageBitmap(bmp);
                    namaMerchant.setText(data.getString(data.getColumnIndex("merchant_name")));
                    alamatMerchant.setText(data.getString(data.getColumnIndex("address")));
                    alamatMerchant2.setText(data.getString(data.getColumnIndex("address")));
                } while (data.moveToNext());
            }
            db.close();
        } catch (Exception er) {

        }


        listMenuCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListMenuRestActivity.this);
        listMenuCategory.setLayoutManager(mLayoutManager);


        new getCategoryMerchant().execute(merchant_id);


    }


    private class getCategoryMerchant extends AsyncTask<String, Void, String> {
        String Resultcek, donlot;
        String[] cat_id, category_name, category_description, photo, status, sequence, date_created, date_modified, category_name_trans, category_description_trans;
        InputStream is = null;
        JSONObject jObj = null;
        String json = "", messg;
        String result, link;
        ArrayList results = new ArrayList<DataObjectListMenu>();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListMenuRestActivity.this);

            pDialog.setMessage("Get category merchant...");

            pDialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);

            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
                nameValuePairs.add(new BasicNameValuePair("merchant_id", params[0]));


                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

                HttpPost httGet = new HttpPost(getResources().getString(R.string.link) + "merchantcategory");
                System.out.println(">>>> " + getResources().getString(R.string.link) + "merchantcategory");


                httGet.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        HTTP.UTF_8));
                HttpResponse httpResponse = httpClient.execute(httGet);
                int code = httpResponse.getStatusLine().getStatusCode();
                System.out.println("Code dari response " + code);


                if (code == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    is.close(); // tutup koneksi stlah medapatkan respone
                    json = sb.toString(); // Respon di jadikan sebuah string
                    System.out.println("hasil request " + json);
                    jObj = new JSONObject(json);
                    result = jObj.getString("code");
                    messg = jObj.getString("msg");
                    System.out.println("Hasil ok " + result);
                    if (result.equals("1") & messg.equals("Successful")) {
                        JSONArray arr = jObj.getJSONObject("details").getJSONArray("category");
                        cat_id = new String[arr.length()];
                        category_name = new String[arr.length()];
                        category_description = new String[arr.length()];
                        photo = new String[arr.length()];
                        status = new String[arr.length()];
                        sequence = new String[arr.length()];
                        date_created = new String[arr.length()];
                        date_modified = new String[arr.length()];
                        category_name_trans = new String[arr.length()];
                        category_description_trans = new String[arr.length()];

                        for (int y = 0; y < arr.length(); y++) {

                            JSONObject ar = arr.getJSONObject(y);
                            cat_id[y] = ar.getString("cat_id");
                            category_name[y] = ar.getString("category_name");
                            category_description[y] = ar.getString("category_description");
                            photo[y] = ar.getString("photo");
                            status[y] = ar.getString("status");
                            sequence[y] = ar.getString("sequence");
                            date_created[y] = ar.getString("date_created");
                            date_modified[y] = ar.getString("date_modified");
                            category_name_trans[y] = ar.getString("category_name_trans");
                            category_description_trans[y] = ar.getString("category_description_trans");


                            System.out.println("NAMA CATEGORII " + category_name[y]);

                            DataObjectListMenu obj = new DataObjectListMenu(cat_id[y], category_name[y], category_description[y], photo[y], status[y],
                                    sequence[y], date_created[y], date_modified[y], category_name_trans[y], category_description_trans[y], merchant_id);
                            results.add(y, obj);

                        }

                    } else {
                        result = "0";
                    }
                } else {
                    result = "false";
                }


            } catch (Exception k) {
                Resultcek = "RTO";
                result = "false";
            }
            return null;

        }

        @Override
        protected void onPostExecute(String hasil) {
            try {
                pDialog.dismiss();

                if (result.equals("1")) {
                    mAdapter = new MyRecyclerViewAdapterListMenu(getApplicationContext(), results);
                    listMenuCategory.setAdapter(mAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), messg, Toast.LENGTH_LONG).show();
                }
            } catch (Exception k) {
                System.out.println("Erro post excute balidate user " + k.getMessage());

            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }


}
