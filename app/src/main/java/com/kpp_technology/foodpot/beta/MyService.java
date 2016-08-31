package com.kpp_technology.foodpot.beta;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;

import com.kpp_technology.foodpot.beta.database.DatabaseHelper;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    public static String statusMerchantList = "No";
    String client_id, api_key;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startid) {
        super.onStart(intent, startid);
        System.out.println("masuk ke onstart ");

        try {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.openDataBase();
            Cursor dat = db.getProfile();
            if (dat.moveToFirst()) {
                do {
                    client_id = dat.getString(dat.getColumnIndex("token"));
                    api_key = "ae5162a18e8afafd29b67b6ec2684813";
                } while (dat.moveToNext());
            }
            db.close();
        } catch (Exception er) {

        }


        new SendMerchant().execute();
        new SendHistoryOrder().execute(client_id, "", api_key);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            new SendMerchant().execute();

        } catch (Exception er) {
            System.out.println("Errorrr startCommand " + er.getMessage());

        }

        return START_STICKY;
    }


    private class SendMerchant extends AsyncTask<String, Void, String> {
        String Resultcek, donlot;
        String token, nextStep, is_checkout, client_id, avatar, client_cookies;
        String[] merchant_id, merchant_name, address, ratings, votes, is_open, logo, map_longitude, map_latitude;
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        String result;
        String messg, pathLogo;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);

                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

                HttpPost httGet = new HttpPost(getResources().getString(R.string.link) + "merchantlist");

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
                    System.out.println("hasil request MERCHAAANNTTTT " + json);
                    jObj = new JSONObject(json);
                    result = jObj.getString("code");
                    messg = jObj.getString("msg");
                    System.out.println("Hasil ok " + result);
                    if (result.equals("1")) {
                        JSONArray arr = jObj.getJSONObject("details").getJSONArray("data");
                        merchant_id = new String[arr.length()];
                        merchant_name = new String[arr.length()];
                        address = new String[arr.length()];
                        ratings = new String[arr.length()];
                        votes = new String[arr.length()];
                        is_open = new String[arr.length()];
                        logo = new String[arr.length()];
                        map_longitude = new String[arr.length()];
                        map_latitude = new String[arr.length()];
                        System.out.println("JUmlah Dataaa " + arr.length());
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.openDataBase();
                        for (int y = 0; y < arr.length(); y++) {
                            JSONObject ar = arr.getJSONObject(y);

                            merchant_id[y] = ar.getString("merchant_id");

                            merchant_name[y] = ar.getString("merchant_name");
                            System.out.println("JUmlah Dataaa " + merchant_name[y]);
                            address[y] = ar.getString("address");
                            ratings[y] = ar.getJSONObject("ratings").getString("ratings");
                            votes[y] = ar.getJSONObject("ratings").getString("votes");
                            is_open[y] = ar.getString("is_open");
                            logo[y] = ar.getString("logo");
                            System.out.println("JUmlah LOGOOO " + logo[y]);
                            map_longitude[y] = ar.getJSONObject("map_coordinates").getString("longitude");
                            map_latitude[y] = ar.getJSONObject("map_coordinates").getString("latitude");

                            System.out.println("JUmlah map_longitude " + map_longitude[y]);
                            System.out.println("JUmlah map_latitude " + map_latitude[y]);


                            System.out.println(">>>>>>>>>>>>>>>  " + merchant_name[y]);
                            System.out.println(">>>>>>>>>>>>>>>  " + logo[y]);

                            try {
                                URL url = new URL(logo[y]);
                                InputStream input = url.openStream();
                                pathLogo = Environment.getExternalStorageDirectory().toString() + "/FootPot/image/logo_" + merchant_id[y] + ".png";
                                File cek = new File(pathLogo);
                                if (cek.exists()) {

                                } else {
                                    OutputStream output = new FileOutputStream(pathLogo);
                                    try {
                                        byte[] buffer = new byte[1024];
                                        int bytesRead = 0;
                                        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                                            output.write(buffer, 0, bytesRead);
                                        }
                                    } finally {
                                        output.close();
                                    }
                                }


                            } catch (Exception er) {
                                System.out.println("Errorrr exception " + er.getMessage());
                            } finally {
                                System.out.println("Selesai image ");
                            }


                            try {
                                System.out.println("Saveee image logo " + pathLogo);
                                db.insertMerchant(merchant_id[y], merchant_name[y], address[y], ratings[y], votes[y], is_open[y], pathLogo, map_latitude[y], map_longitude[y]);
                            } catch (Exception er) {
                                System.out.println("Error save data karena " + er.getMessage());
                            }

                        }
                        db.close();

                    } else {
                        result = "0";
                    }
                } else {
                    result = "false";
                }


            } catch (Exception e) {
                System.out.println("Errrorr karena " + e.getMessage());
                Resultcek = "RTO";
                result = "false";
            }

            return null;

        }

        @Override
        protected void onPostExecute(String hasil) {
            try {
                statusMerchantList = "Yes";
                if (result.equals("1")) {


                } else {

                }

            } catch (Exception k) {
                System.out.println("Erro post excute balidate user " + k.getMessage());

            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }


    private class SendHistoryOrder extends AsyncTask<String, Void, String> {
        String Resultcek, donlot;

        String[] order_id, title, status;
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        String result;
        String messg, pathLogo;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
                nameValuePairs.add(new BasicNameValuePair("client_token", params[0]));
                nameValuePairs.add(new BasicNameValuePair("lang_id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("api_key", params[2]));

                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

                HttpPost httGet = new HttpPost(getResources().getString(R.string.link) + "getOrderHistory");


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
                    if (result.equals("1")) {
                        JSONArray arr = jObj.getJSONArray("details");
                        order_id = new String[arr.length()];
                        title = new String[arr.length()];
                        status = new String[arr.length()];

                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

                        db.openDataBase();
                        for (int y = 0; y < arr.length(); y++) {
                            JSONObject ar = arr.getJSONObject(y);
                            order_id[y] = ar.getString("order_id");
                            title[y] = ar.getString("title");
                            status[y] = ar.getString("status");


                            try {
                                //  System.out.println("Saveee image logo " + pathLogo);
                                db.insertHistoryOrder(order_id[y], title[y], status[y]);
                            } catch (Exception er) {

                            }

                        }
                        db.close();

                    } else {
                        result = "0";
                    }
                } else {
                    result = "false";
                }


            } catch (Exception k)

            {
                Resultcek = "RTO";
                result = "false";
            }

            return null;

        }

        @Override
        protected void onPostExecute(String hasil) {
            try {

                if (result.equals("1")) {


                } else {

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
