package com.netchosis.somthing.project2_phone2;

import android.app.Application;
import android.preference.PreferenceManager;
import android.util.Log;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nigel on 4/22/15.
 */
public class Settings extends Application {
    public String SIP_URL = Urls.SIP_URL;
    public String USERS_URL = Urls.SIP_USERS_URL;
    public String PERF_NAME = "test";
    public String PERF_LOGIN = "login";
    public String FAIL = "fail";
    public SharedPreferences app_perfs;


    // -------------Start sip user ------------------//
    private String SIP_USER = null;

    public String get_SIP_USER() {
        return SIP_USER;
    }
    public void set_SIP_USER(String SIP_USER) {
        this.SIP_USER = SIP_USER;
    }

    //------------Start sip password ---------------//
    private  String SIP_PASSWORD = null;
    public String get_SIP_PASSWORD() {
        return SIP_PASSWORD;
    }

    public void setSIP_PASSWORD(String SIP_PASSWORD) {
        this.SIP_PASSWORD = SIP_PASSWORD;
    }

    //-----------Start HTTP TOKEN ----------------//
    private String HTTP_TOKEN;
    public  String getHTTP_TOKEN(){
        return HTTP_TOKEN;
    }

    public void setHTTP_TOKEN(String HTTP_TOKEN){
        this.HTTP_TOKEN = HTTP_TOKEN;
    }

    //-----------------Convert Stream to String------------------//
    public static String convertStreamToString(InputStream is) { //  this thing convers
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();


        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line +"\n" );
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    //-----------Start Shared Preferances -------------------------//
    public void store_tokens(String HTTP_TOKEN) {

        SharedPreferences.Editor editor = this.app_perfs.edit();
        editor.putString(PERF_NAME,HTTP_TOKEN);
        editor.commit();
    }


    public void onCreate(){
        super.onCreate();
        Log.d("shit", "is being called");
        app_perfs = getSharedPreferences(PERF_NAME,0);
        SharedPreferences.Editor editor  = app_perfs.edit();
        editor.putString(PERF_NAME,FAIL);
        editor.commit();
        Log.d("shit", "is being called");

        if (this.getloginstate() =="no"){
            HTTP_TOKEN = app_perfs.getString(PERF_NAME,FAIL);
        }
    }

    public String getloginstate(){
        Log.d("Getloginstate","getlogin state");

        String test = this.app_perfs.getString("test","ass");
        if (test == FAIL){
            return "yes";
        }
        return "no";
    }


}
