package com.kpp_technology.foodpot.beta.fragmentLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kpp_technology.foodpot.beta.BerandaActivity;
import com.kpp_technology.foodpot.beta.ForgetActivity;
import com.kpp_technology.foodpot.beta.MyService;
import com.kpp_technology.foodpot.beta.R;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SignInFragment extends Fragment {
    Button buttonSignIn;
    EditText editPass, editUsername;
    TextView textPass, textUsername, textPassForget;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        buttonSignIn = (Button) view.findViewById(R.id.buttonSignIn);
        editPass = (EditText) view.findViewById(R.id.editPass);
        textUsername = (TextView) view.findViewById(R.id.textUsername);
        textPass = (TextView) view.findViewById(R.id.textPass);
        textPassForget = (TextView) view.findViewById(R.id.textPassForget);

        editUsername = (EditText) view.findViewById(R.id.editUsername);
        //   editUsername.setText("http://somenoise.esy.es/mobileapp/api/login?email_address=agung%40gmail.com&password=12345678");
        textPassForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent pindah = new Intent(getActivity(), ForgetActivity.class);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pindah);
                } catch (Exception er) {

                }
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String user = editUsername.getText().toString();
                    String passs = editPass.getText().toString();
                    new SendSignIn().execute(user, passs);
                } catch (Exception er) {

                }
            }
        });

        return view;
    }

    private class SendSignIn extends AsyncTask<String, Void, String> {
        String Resultcek, donlot;
        String token, nextStep, has_addressbook, avatar, client_cookies;
        InputStream is = null;
        JSONObject jObj = null;
        String json = "", messg;
        String result, link;

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());

            pDialog.setMessage("Connecting to server");

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

                nameValuePairs.add(new BasicNameValuePair("email_address", params[0]));
                nameValuePairs.add(new BasicNameValuePair("password", params[1]));

                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

                HttpPost httGet = new HttpPost(getResources().getString(R.string.link) + "login");

                // link = "http://somenoise.esy.es/mobileapp/api/login?" + paramString;
                // System.out.println("http://somenoise.esy.es/mobileapp/api/login?" + paramString);


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
                        token = jObj.getJSONObject("details").getString("token");
                        nextStep = jObj.getJSONObject("details").getString("next_steps");
                        has_addressbook = jObj.getJSONObject("details").getString("has_addressbook");
                        avatar = jObj.getJSONObject("details").getString("avatar");
                        client_cookies = jObj.getJSONObject("details").getString("client_name_cookie");

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
                    Toast.makeText(getActivity().getApplicationContext(), messg, Toast.LENGTH_LONG).show();

                    new getProfile().execute(token);

                   /* Intent pindah = new Intent(getActivity(), BerandaActivity.class);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pindah);*/

                    Intent start = new Intent(getActivity(), MyService.class);
                    getActivity().startService(start);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), messg, Toast.LENGTH_LONG).show();
                }
            } catch (Exception k) {
                System.out.println("Erro post excute balidate user " + k.getMessage());

            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }


    private class getProfile extends AsyncTask<String, Void, String> {
        String Resultcek, donlot;
        String token, nextStep, has_addressbook, avatar, client_cookies;
        InputStream is = null;
        JSONObject jObj = null;
        String json = "", messg;
        String result, link;

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());

            pDialog.setMessage("Get Profile Login...");

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
                nameValuePairs.add(new BasicNameValuePair("client_token", params[0]));


                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

                HttpPost httGet = new HttpPost(getResources().getString(R.string.link) + "getprofile");

                // link = "http://somenoise.esy.es/mobileapp/api/login?" + paramString;

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
                        try {
                            DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());

                            db.openDataBase();
                            String client_id = jObj.getJSONObject("details").getString("client_id");
                            String social_strategy = jObj.getJSONObject("details").getString("social_strategy");
                            String first_name = jObj.getJSONObject("details").getString("first_name");
                            String last_name = jObj.getJSONObject("details").getString("last_name");
                            String email_address = jObj.getJSONObject("details").getString("email_address");
                            String password = jObj.getJSONObject("details").getString("password");
                            String saldo = jObj.getJSONObject("details").getString("saldo");
                            String street = jObj.getJSONObject("details").getString("street");
                            String city = jObj.getJSONObject("details").getString("city");
                            String state = jObj.getJSONObject("details").getString("state");
                            String zipcode = jObj.getJSONObject("details").getString("zipcode");
                            String country_code = jObj.getJSONObject("details").getString("country_code");
                            String location_name = jObj.getJSONObject("details").getString("location_name");
                            String contact_phone = jObj.getJSONObject("details").getString("contact_phone");
                            String lost_password_token = jObj.getJSONObject("details").getString("lost_password_token");
                            String date_created = jObj.getJSONObject("details").getString("date_created");
                            String date_modified = jObj.getJSONObject("details").getString("date_modified");
                            String last_login = jObj.getJSONObject("details").getString("last_login");
                            String ip_address = jObj.getJSONObject("details").getString("ip_address");
                            String status = jObj.getJSONObject("details").getString("status");
                            String token = jObj.getJSONObject("details").getString("token");
                            String client_token = jObj.getJSONObject("request").getString("client_token");
                            String avatar = jObj.getJSONObject("details").getString("avatar");
                            String email_verification_code = jObj.getJSONObject("details").getString("email_verification_code");
                            String status_login = "Ya";

                            try {
                                db.deleteProfile();
                            } catch (Exception er) {
                                System.out.println("Error delet " + er.getMessage());
                            }

                            db.insertProfile(client_id, social_strategy, first_name, last_name, email_address, password, street, city, state, zipcode, country_code, location_name, contact_phone, lost_password_token, date_created, date_modified, last_login, status, token, avatar, client_token, status_login, saldo);
                            db.close();
                        } catch (Exception er) {
                            System.out.println("Error login to DB " + er.getMessage());
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
                    Toast.makeText(getActivity().getApplicationContext(), messg, Toast.LENGTH_LONG).show();

                    Intent pindah = new Intent(getActivity(), BerandaActivity.class);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pindah);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), messg, Toast.LENGTH_LONG).show();
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
