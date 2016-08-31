package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kpp_technology.foodpot.beta.R;
import com.kpp_technology.foodpot.beta.adapter.MyRecyclerViewAdapterListMenuOrder;
import com.kpp_technology.foodpot.beta.itemObject.DataObjectListMenuOrder;

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
import java.util.Calendar;
import java.util.List;

public class MenuOrderActivity extends Activity {
    RecyclerView listOrderMenu;
    String cat_id, merchant_id, cate_name;
    MyRecyclerViewAdapterListMenuOrder mAdapter;
    TextView Judul;
    public static TextView textJumlahOrder, jumlahHarga;
    ImageView imageNext;
    public static int jumlahSemuanya = 0;
    public static int jumlahHargaSemuanya = 0;
    public static int weightSemuanya = 0;

    // DatePicker
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    String dateKirim, TimeKirim;

    public static ArrayList<String> nameItem = new ArrayList<String>();
    public static ArrayList<Integer> jumlahItem = new ArrayList<>();
    public static ArrayList<String> hargaItem = new ArrayList<>();


    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent ambil = getIntent();
        cat_id = ambil.getStringExtra("cat_id");
        merchant_id = ambil.getStringExtra("merchant_id");
        cate_name = ambil.getStringExtra("cate_name");


        Judul = (TextView) findViewById(R.id.Judul);
        textJumlahOrder = (TextView) findViewById(R.id.textJumlahOrder);
        jumlahHarga = (TextView) findViewById(R.id.jumlahHarga);
        imageNext = (ImageView) findViewById(R.id.imageNext);

        listOrderMenu = (RecyclerView) findViewById(R.id.listOrderMenu);
        listOrderMenu.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MenuOrderActivity.this);
        listOrderMenu.setLayoutManager(mLayoutManager);
        Judul.setText(cate_name);
        new getItemByCategoryMerchant().execute(merchant_id, cat_id);

        try {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } catch (Exception er) {
            System.out.println("Errrod ate pickerrr " + er.getMessage());
        }


        imageNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                // showDate(year, month+1, day);


                Dialog dialog = new Dialog(MenuOrderActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_pop_order_type);
                LinearLayout linearEcomnomy = (LinearLayout) dialog.findViewById(R.id.linearEcomnomy);
                LinearLayout linearExpree = (LinearLayout) dialog.findViewById(R.id.linearExpree);

                linearExpree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(999);
                    }
                });
                linearEcomnomy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                dialog.show();
            }
        });


    }

    public static class CustomTimePickerDialog extends TimePickerDialog {

        public static final int TIME_PICKER_INTERVAL = 15;
        private boolean mIgnoreEvent = false;

        public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
            super(context, callBack, hourOfDay, minute, is24HourView);
        }

        @Override
        public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
            super.onTimeChanged(timePicker, hourOfDay, minute);
            if (!mIgnoreEvent) {
                minute = getRoundedMinute(minute);
                mIgnoreEvent = true;
                timePicker.setCurrentMinute(minute);
                mIgnoreEvent = false;
            }
        }

        public static int getRoundedMinute(int minute) {
            if (minute % TIME_PICKER_INTERVAL != 0) {
                int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
                minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
                if (minute == 60) minute = 0;
            }

            return minute;
        }
    }

    private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // txt_time.setText(String.format("%02d", hourOfDay) + ":" +String.format("%02d", minute));
            System.out.println(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));

            TimeKirim = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);


            for (int r = 0; r < nameItem.size(); r++) {
                System.out.println(r + " > " + nameItem.get(r).toString());
            }
            for (int d = 0; d < jumlahItem.size(); d++) {
                System.out.println(d + "JUMLAH > " + jumlahItem.get(d).toString());

            }


            Intent pindah = new Intent(MenuOrderActivity.this, CheckoutPayment.class);
            pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pindah.putExtra("time", TimeKirim);
            pindah.putExtra("date", dateKirim);
            pindah.putExtra("weight", String.valueOf(weightSemuanya));
            pindah.putExtra("harga", String.valueOf(jumlahHargaSemuanya));
            startActivity(pindah);


        }
    };

    public static void updateBawah(int tam, int harga, int weight) {
        // System.out.print("UPDATEEEE");

        weightSemuanya = weightSemuanya + weight;

        jumlahSemuanya = jumlahSemuanya + tam;
        jumlahHargaSemuanya = jumlahHargaSemuanya + harga;
        textJumlahOrder.setText(String.valueOf(jumlahSemuanya));
        jumlahHarga.setText("Rp. " + jumlahHargaSemuanya);
    }

    public static void updateBawahRemove(int tam, int harga, int weight) {
        System.out.print("UPDATEEEE");
        weightSemuanya = weightSemuanya - weight;
        jumlahSemuanya = jumlahSemuanya - tam;
        jumlahHargaSemuanya = jumlahHargaSemuanya - harga;
        textJumlahOrder.setText(String.valueOf(jumlahSemuanya));
        jumlahHarga.setText("Rp. " + jumlahHargaSemuanya);
    }

    private class getItemByCategoryMerchant extends AsyncTask<String, Void, String> {
        String Resultcek, donlot;
        String[] category, item_id, item_name, item_description, discount, weight, uom, stock, views, sold, min_order, sequence, is_featured, photo, not_available, retail_price, currency_retail_price;
        InputStream is = null;
        JSONObject jObj = null;
        String json = "", messg;
        String result, link;
        ArrayList results = new ArrayList<DataObjectListMenuOrder>();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuOrderActivity.this);

            pDialog.setMessage("Get item category...");

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
                nameValuePairs.add(new BasicNameValuePair("cat_id", params[1]));


                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

                HttpPost httGet = new HttpPost(getResources().getString(R.string.link) + "getitembycategory");
                System.out.println(">>>> " + getResources().getString(R.string.link) + "getitembycategory");


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
                        JSONArray arr = jObj.getJSONObject("details").getJSONArray("item");
                        category = new String[arr.length()];
                        item_id = new String[arr.length()];
                        item_name = new String[arr.length()];
                        item_description = new String[arr.length()];
                        discount = new String[arr.length()];
                        weight = new String[arr.length()];
                        uom = new String[arr.length()];
                        stock = new String[arr.length()];
                        views = new String[arr.length()];
                        sold = new String[arr.length()];
                        min_order = new String[arr.length()];
                        sequence = new String[arr.length()];
                        is_featured = new String[arr.length()];
                        photo = new String[arr.length()];
                        not_available = new String[arr.length()];
                        retail_price = new String[arr.length()];
                        currency_retail_price = new String[arr.length()];


                        for (int y = 0; y < arr.length(); y++) {
                            JSONObject ar = arr.getJSONObject(y);
                            category[y] = ar.getString("category");
                            item_id[y] = ar.getString("item_id");
                            item_name[y] = ar.getString("item_name");
                            item_description[y] = ar.getString("item_description");
                            discount[y] = ar.getString("discount");
                            weight[y] = ar.getString("weight");
                            uom[y] = ar.getString("uom");
                            stock[y] = ar.getString("stock");
                            views[y] = ar.getString("views");
                            sold[y] = ar.getString("sold");
                            min_order[y] = ar.getString("min_order");
                            sequence[y] = ar.getString("sequence");
                            is_featured[y] = ar.getString("is_featured");
                            photo[y] = ar.getString("photo");
                            not_available[y] = ar.getString("not_available");
                            retail_price[y] = ar.getString("retail_price");
                            currency_retail_price[y] = ar.getString("currency_retail_price");


                            DataObjectListMenuOrder obj = new DataObjectListMenuOrder(category[y], item_id[y], item_name[y], item_description[y],
                                    discount[y], weight[y], uom[y], stock[y], views[y], sold[y], min_order[y], sequence[y], is_featured[y],
                                    photo[y], not_available[y], retail_price[y], currency_retail_price[y], merchant_id);
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
                    mAdapter = new MyRecyclerViewAdapterListMenuOrder(getApplicationContext(), results);
                    listOrderMenu.setAdapter(mAdapter);

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


    private void showDate(int year, int month, int day) {
       /* dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));*/

        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(MenuOrderActivity.this, timeSetListener,
                Calendar.getInstance().get(Calendar.HOUR),
                CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
        timePickerDialog.setTitle("Set hours and minutes");
        timePickerDialog.show();

        dateKirim = "" + day + "-" + month + "-" + year;


    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

}
