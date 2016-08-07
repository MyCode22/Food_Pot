package com.kpp_technology.foodpot.beta.storage;

import android.os.Environment;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Mobile Tech on 11/19/2015.
 */
public class StorageResource {


    public static boolean Storage() {

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/FootPot";

            File myNewFolder = new File(path);
            if (!myNewFolder.exists()) {
                myNewFolder.mkdir();
            }
            StorageImageLogo();
        } catch (Exception er) {

        }
        return true;
    }

    public static boolean StorageImageLogo() {

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/FootPot/image";

            File myNewFolder = new File(path);
            if (!myNewFolder.exists()) {
                myNewFolder.mkdir();
            }
        } catch (Exception er) {

        }
        return true;
    }

    public static String md5() {

        String campur2 = "FootPot V-01";
        StringBuffer hexString = new StringBuffer();
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(campur2.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String

            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            hexString = null;
        }
        return hexString.toString();
    }
}
