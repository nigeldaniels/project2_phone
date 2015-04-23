package com.netchosis.somthing.project2_phone2;

/**
 * Created by nigel on 4/22/15.
 *
 */
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.sip.SipAudioCall;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class netdata extends AsyncTask<String, Void, JSONArray>   {

    public JSONArray users;
    private String token;
    private Context mcontext;
    //private String stoken;


    private static String convertStreamToString(InputStream is) { //  this thing convers
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


    public void setToken(String token) {
        Log.d("NETDATA", token);
        this.token = "Token " + token;

    }

    private JSONArray connect(String url) throws IOException, JSONException {
        String value = token;

        HttpClient httpclient = new DefaultHttpClient();

        try{
            HttpGet httpget = new HttpGet(url);
            httpget.addHeader("Authorization", value);

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null){
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);

                Log.i("Users:",result);
                JSONArray json = new JSONArray(result);
                instream.close();
                users = json;
                return users;
            }
        }

        catch (IOException e){
            e.printStackTrace();
        }
        return users;
    }


    public JSONArray getUsers() {
        return users;
    }

    @Override
    protected JSONArray doInBackground(String... url) {
        try {

            return connect(url[0]);
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    protected void onPostExecute (JSONArray result){

    }

}

