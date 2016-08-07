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
import android.widget.Toast;

import com.kpp_technology.foodpot.beta.LoginActivity;
import com.kpp_technology.foodpot.beta.R;

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


public class ForgetPassFragment extends Fragment {

    public ForgetPassFragment() {
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
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        Button buttonReset = (Button) view.findViewById(R.id.buttonReset);
        final EditText editEmailForget = (EditText) view.findViewById(R.id.editEmailForget);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = editEmailForget.getText().toString();
                    new ForgotPass().execute(email);
                } catch (Exception er) {

                }
            }
        });

        return view;
    }

    private class ForgotPass extends AsyncTask<String, Void, String> {
        String Resultcek, donlot;
        String token, nextStep, is_checkout, client_id, avatar, client_cookies;
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        String result;
        String messg;
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

                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

                HttpPost httGet = new HttpPost("http://somenoise.esy.es/mobileapp/api/forgotpassword");

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
                    Intent pindah = new Intent(getActivity(), LoginActivity.class);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
