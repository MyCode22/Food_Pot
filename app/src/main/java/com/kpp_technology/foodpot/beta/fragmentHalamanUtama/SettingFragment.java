package com.kpp_technology.foodpot.beta.fragmentHalamanUtama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kpp_technology.foodpot.beta.BerandaActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SettingFragment extends Fragment {
    LinearLayout layout2, layout1, layoutDefaultPayment;
    LinearLayout layoutProfile, layoutPayment, layoutPoint;
    EditText editPhone, editName, editEmail, editLastName;
    Button cancel_edit, save_edit;
    String token;

    TextView saldoUser;
    RadioGroup typePembayaran;
    EditText editVoucher;
    Button redeem;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        layout1 = (LinearLayout) view.findViewById(R.id.layout1);
        layout2 = (LinearLayout) view.findViewById(R.id.layout2);
        layoutDefaultPayment = (LinearLayout) view.findViewById(R.id.layoutDefaultPayment);


        layoutProfile = (LinearLayout) view.findViewById(R.id.layoutProfile);
        layoutPayment = (LinearLayout) view.findViewById(R.id.layoutPayment);
        layoutPoint = (LinearLayout) view.findViewById(R.id.layoutPoint);


        editPhone = (EditText) view.findViewById(R.id.editPhone);
        editName = (EditText) view.findViewById(R.id.editName);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        editLastName = (EditText) view.findViewById(R.id.editLastName);
        cancel_edit = (Button) view.findViewById(R.id.cancel_edit);
        save_edit = (Button) view.findViewById(R.id.save_edit);


        cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout2.setVisibility(View.GONE);
                layoutDefaultPayment.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
            }
        });

        save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String fir = editName.getText().toString();
                    String las = editLastName.getText().toString();
                    String em = editEmail.getText().toString();
                    String ph = editPhone.getText().toString();
                    Date cDate = new Date();
                    String fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cDate);
                    new SaveProfile().execute(token, fir, las, ph, fDate);
                } catch (Exception er) {
                    System.out.println("Errorrr save profile " + er.getMessage());
                }
            }
        });


        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    layout2.setVisibility(View.VISIBLE);
                    layout1.setVisibility(View.GONE);
                    layoutDefaultPayment.setVisibility(View.GONE);

                    DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
                    db.openDataBase();
                    Cursor data = db.getProfile();
                    if (data.moveToFirst()) {
                        do {
                            editPhone.setText(data.getString(data.getColumnIndex("contact_phone")));
                            editName.setText(data.getString(data.getColumnIndex("first_name")));
                            editLastName.setText(data.getString(data.getColumnIndex("last_name")));
                            editEmail.setText(data.getString(data.getColumnIndex("email_address")));
                            token = data.getString(data.getColumnIndex("client_token"));
                        } while (data.moveToNext());
                    }


                } catch (Exception er) {
                    System.out.println("Errorrr brr " + er.getMessage());
                }

            }
        });

        /**
         *                      Default Pembayaran
         */

        saldoUser = (TextView) view.findViewById(R.id.saldoUser);
        typePembayaran = (RadioGroup) view.findViewById(R.id.typePembayaran);
        editVoucher = (EditText) view.findViewById(R.id.editVoucher);
        redeem = (Button) view.findViewById(R.id.redeem);


        typePembayaran.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.credit) {
                    editVoucher.setVisibility(View.VISIBLE);
                } else if (i == R.id.cash) {
                    editVoucher.setVisibility(View.GONE);
                }


            }
        });

        layoutPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    layout2.setVisibility(View.GONE);
                    layout1.setVisibility(View.GONE);
                    layoutDefaultPayment.setVisibility(View.VISIBLE);

                    DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
                    db.openDataBase();
                    Cursor data = db.getProfile();
                    if (data.moveToFirst()) {
                        do {
                            saldoUser.setText(data.getString(data.getColumnIndex("saldo")));
                        } while (data.moveToNext());
                    }
                    db.close();

                } catch (Exception er) {

                }
            }
        });


        return view;
    }

    private class SaveProfile extends AsyncTask<String, Void, String> {
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
                nameValuePairs.add(new BasicNameValuePair("client_token", params[0]));
                nameValuePairs.add(new BasicNameValuePair("first_name", params[1]));
                nameValuePairs.add(new BasicNameValuePair("last_name", params[2]));
                nameValuePairs.add(new BasicNameValuePair("contact_phone", params[3]));
                nameValuePairs.add(new BasicNameValuePair("date_modified", params[4]));
                nameValuePairs.add(new BasicNameValuePair("ip_address", ""));

                System.out.println("params 1 " + params[0]);
                System.out.println("params 1 " + params[1]);
                System.out.println("params 1 " + params[2]);
                System.out.println("params 1 " + params[3]);
                System.out.println("params 1 " + params[4]);

                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

                System.out.println(" >>>> " + "http://somenoise.esy.es/mobileapp/api/saveprofile");

                HttpPost httGet = new HttpPost(getResources().getString(R.string.link) + "saveprofile");

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
                System.out.println("Errrorrr " + k.getMessage());
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
