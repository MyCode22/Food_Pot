package com.kpp_technology.foodpot.beta.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.kpp_technology.foodpot.beta.storage.StorageResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    // The Android's default system path of your application database.
    public static String DB_NAME = "foodPot";
    public SQLiteDatabase myDataBase;
    private static Context myContext = null;

    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */
    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        if (myContext == null)
            myContext = context;
    }

    public void createDataBase(String md5) throws IOException {
        boolean dbExist = checkDataBase(md5);
        System.out.println("Hasil cek DB " + dbExist);
        if (dbExist == true) {

        } else {
            System.out.println("copy DB");
            createDataBaseSqlite(md5);
        }
        //
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBaseSqlite(String md5) {
        try {
            String fpath = Environment.getExternalStorageDirectory().toString() + "/FootPot/" + md5;
            System.out.println("MMMMMMMMMMMM " + fpath);
            File file = new File(fpath);
            if (!file.exists()) {
                //FILE BLOM ADA
                System.out.println("FILE BLEUM ADA");
                try {
                    //File newFolder3 = new File(SplashActivity.urlSdcard);
                    // File file3 = new File(newFolder3, "speak" + ".db");
                    this.getReadableDatabase();
                    // file3.createNewFile();
                    copyDataBase(md5);
                    System.out.println("Mulai copy File to sqlite");
                } catch (Exception ex) {
                    System.out.println("ex: " + ex);
                }
            } else {
                System.out.println("FILE EXIST..!!");
            }
        } catch (Exception er) {
            System.out.println("Errror read CSV file " + er.getMessage());
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @param md5
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(String md5) {
        SQLiteDatabase checkDB = null;
        try {
            System.out.println("Masuk ke cek DB");
            //String myPath = SplashActivity.urlSdcard + "/speak.db";
            String myPath = Environment.getExternalStorageDirectory().toString() + "/FootPot/" + md5;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     *
     * @param md5
     */
    private void copyDataBase(String md5) throws IOException {
        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open("foodPot");
        // Path to the just created empty db
        String outFileName = Environment.getExternalStorageDirectory().toString() + "/FootPot/" + md5;

        System.out.println("Dari database " + myInput);
        System.out.println(outFileName);
        System.out.println("Copy database berhasil ..!!!");
        System.out.println("Copy database berhasil ..!!! >> " + outFileName);
        Log.e("cdb", outFileName);
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        try {
            String md5 = StorageResource.md5();
            // Open the database
            String myPath = Environment.getExternalStorageDirectory().toString() + "/FootPot/" + md5;
            File asb = new File(myPath);
            if (asb.exists()) {
                //System.out.println("DB " + user + " sudah ada IO Socket " + ServiceData.socketId);
                myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READWRITE);
            } else {
                System.out.println("Database yang di maksd tidak ada " + myPath);
                //System.out.println(Environment.getExternalStorageDirectory().toString()+"/Pertagas" + user + "/" + user + ".db");
            }
            // System.out.println(">>> "+myPath);

        } catch (Exception er) {

            System.out.println("Error open DB karen " + er.getMessage());
        }


    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean doesTblExist(String tblName) {

        Cursor rs = null;
        try {
            System.out.println("Masuk ke dalam query doesTblExist " + tblName);
            rs = myDataBase.rawQuery("SELECT * FROM " + tblName, null);
            System.out.println("Hasilnya CEK TABLE " + tblName + " >>>>>> " + rs.getCount());
            if (rs.getCount() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * "  INSERT TABLE  "
     */

    public void insertProfile(String client_id, String social_strategy, String first_name, String last_name,
                              String email_address, String password, String street, String city, String state,
                              String zipcode, String country_code, String location_name, String contact_phone,
                              String lost_password_token, String date_created, String date_modified, String last_login,
                              String status, String token, String avatar, String client_token, String status_login) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("client_id", client_id);
            cv.put("social_strategy", social_strategy);
            cv.put("first_name", first_name);
            cv.put("last_name", last_name);
            cv.put("email_address", email_address);
            cv.put("password", password);
            cv.put("street", street);
            cv.put("city", city);
            cv.put("state", state);
            cv.put("zipcode", zipcode);
            cv.put("country_code", country_code);
            cv.put("location_name", location_name);
            cv.put("contact_phone", contact_phone);
            cv.put("lost_password_token", lost_password_token);
            cv.put("date_created", date_created);
            cv.put("date_modified", date_modified);
            cv.put("last_login", last_login);
            cv.put("status", status);
            cv.put("token", token);
            cv.put("avatar", avatar);
            cv.put("client_token", client_token);
            cv.put("status_login", status_login);

            myDataBase.insert("profile", null, cv);
        } catch (Exception er) {
            System.out.println("Error query ke profile " + er.getMessage());
        }

    }

    /**
     * table onsert to tracker Param jika param tidak terkirim,,
     */
    public void insertMerchant(String merchant_id, String merchant_name, String address, String ratings,
                               String votes, String is_open, String logo,
                               String map_latitude, String map_longitude) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("merchant_id", merchant_id);
            cv.put("merchant_name", merchant_name);
            cv.put("address", address);
            cv.put("ratings", ratings);
            cv.put("votes", votes);
            cv.put("is_open", is_open);
            cv.put("logo", logo);
            cv.put("map_latitude", map_latitude);
            cv.put("map_longitude", map_longitude);

            myDataBase.insert("merchant", null, cv);
        } catch (Exception er) {
            System.out.println("Error query ke merchant " + er.getMessage());
        }

    }

    public void insertHistoryOrder(String order_id, String title, String status) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("order_id", order_id);
            cv.put("title", title);
            cv.put("status", status);
            myDataBase.insert("history_order", null, cv);
        } catch (Exception er) {
            System.out.println("Error query ke trackerParameter " + er.getMessage());
        }

    }


    /**
     * " QUERY UPDATE "
     */

    public boolean updatePassword(String password, String imei) {
        try {
            ContentValues args = new ContentValues();
            args.put("password", password);
            return myDataBase.update("setting", args, "imei" + "= '" + imei
                    + "'", null) > 0;
        } catch (Exception er) {
            System.out.println("Error di update user for token " + er.getMessage());
            return false;

        }
    }

    public boolean updateStatus(String status, String imei) {
        try {
            ContentValues args = new ContentValues();
            args.put("statusServiceRunning", status);
            return myDataBase.update("setting", args, "imei" + "= '" + imei
                    + "'", null) > 0;
        } catch (Exception er) {
            System.out.println("Error di update user for token " + er.getMessage());
            return false;

        }
    }

    public boolean updateInterval(String interval, String imei) {
        try {
            ContentValues args = new ContentValues();
            args.put("interval", interval);
            return myDataBase.update("setting", args, "imei" + "= '" + imei
                    + "'", null) > 0;
        } catch (Exception er) {
            System.out.println("Error di update user for token " + er.getMessage());
            return false;

        }
    }

    public boolean updateUserApproval(String imei, String user_aproval, String date_approval) {
        try {
            ContentValues args = new ContentValues();
            args.put("approval", date_approval);
            args.put("admin_approval", user_aproval);
            return myDataBase.update("setting", args, "imei" + "= '" + imei
                    + "'", null) > 0;
        } catch (Exception er) {
            System.out.println("Error di update user for token " + er.getMessage());
            return false;

        }
    }

    public boolean updateStartUp(String start, String imei) {
        try {
            ContentValues args = new ContentValues();
            args.put("start", start);
            return myDataBase.update("setting", args, "imei" + "= '" + imei
                    + "'", null) > 0;
        } catch (Exception er) {
            System.out.println("Error di update user for token " + er.getMessage());
            return false;

        }
    }

    public boolean updateLink(String link, String imei) {
        try {
            ContentValues args = new ContentValues();
            args.put("link", link);
            return myDataBase.update("setting", args, "imei" + "= '" + imei
                    + "'", null) > 0;
        } catch (Exception er) {
            System.out.println("Error di update user for token " + er.getMessage());
            return false;

        }
    }

    public boolean updatStatusParameterPending(String id) {
        try {
            ContentValues args = new ContentValues();
            args.put("sent", "1");
            return myDataBase.update("trackerParamater", args, "id" + "= '" + id
                    + "'", null) > 0;
        } catch (Exception er) {
            System.out.println("Error di update trackerParamater " + er.getMessage());
            return false;

        }
    }


    /**
     * " QUERY SELECT "
     */
    public Cursor getProfile() {
        try {
            return myDataBase.query("profile", new String[]{"client_id", "social_strategy", "first_name", "last_name",
                            "email_address", "password", "street", "city", "state", "zipcode", "country_code", "location_name",
                            "contact_phone", "lost_password_token", "date_created", "date_modified", "last_login", "ip_address",
                            "status", "token", "avatar", "client_token", "email_verification_code", "status_login"},
                    null, null, null, null, null);
        } catch (Exception er) {
            System.out.println("Errorrrr getSetting" + er.getMessage());
            return null;
        }

    }

    public Cursor getAllMerchant() {
        try {
            return myDataBase.query("merchant", new String[]{"merchant_id", "merchant_name", "address", "ratings", "votes", "is_open", "logo", "map_latitude", "map_longitude"},
                    null, null, null, null, null);
        } catch (Exception er) {
            System.out.println("Errorrrr getAllMerchant" + er.getMessage());
            return null;
        }

    }

    public Cursor getHistoryOrder(String status) {
        try {
            return myDataBase.query("history_order", new String[]{"order_id", "title", "status"},
                    "status='" + status + "'", null, null, null, null);
        } catch (Exception er) {
            System.out.println("Errorrrr " + er.getMessage());
            return null;
        }

    }

    public Cursor getAllPhoneNumber() {
        try {
            return myDataBase.query("phone", new String[]{"id", "number"},
                    null, null, null, null, null);
        } catch (Exception er) {
            System.out.println("Errorrrr " + er.getMessage());
            return null;
        }

    }

    public Cursor getPhoneByNumber(String number) {
        try {
            return myDataBase.query("phone", new String[]{"id", "number"},
                    "number='" + number + "'", null, null, null, null);
        } catch (Exception er) {
            System.out.println("Errorrrr " + er.getMessage());
            return null;
        }
    }

    /**
     * QUERY UPDATE
     */

    public boolean updateDistanceMerchant(String merchant_id, String distance) {
        try {
            ContentValues args = new ContentValues();
            args.put("distance", distance);
            return myDataBase.update("merchant", args,
                    "merchant_id" + "= '" + merchant_id + "'", null) > 0;
        } catch (Exception er) {
            System.out.println("Errorr update URUTAN " + er.getMessage());
            return false;
        }
    }


    public boolean deleteProfile() {
        return myDataBase.delete("profile", null, null) > 0;
    }

    public boolean deletPhoneNumber(String number) {
        return myDataBase.delete("phone", "number" + "=" + number, null) > 0;
    }


}
