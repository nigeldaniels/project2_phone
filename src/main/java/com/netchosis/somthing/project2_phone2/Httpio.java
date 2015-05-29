package com.netchosis.somthing.project2_phone2;

/**
 * Created by nigel on 4/22/15.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Httpio extends AsyncTask<String,Void,String>  {
    public String authstring = "no";
    public String json;
    public Handler httphandler;
    private boolean get;
    private boolean post;
    private List<NameValuePair> data;

    public void setPost(){
        this.get = false;
        this.post = true;
    }

    public void setGet(){
        this.post = false;
        this.get = true;
    }

    public void setAuthstring(String Authstring){
        this.authstring ="Token " +Authstring;
    }

    public String getAuthstring(){
        return this.authstring;
    }

    public Httpio(Handler httphandler){
        this.httphandler = httphandler;
    }

    public void sendData(List<NameValuePair> json){
        this.data = json;
    }

    @Override
    protected String doInBackground(String... urls) {

        if (post == true) {
            return this.post(urls[0]);
        }

        if (get == true){
            return this.get(urls[0]);
        }

        return this.post(urls[0]);
    }

    public String url;

    private static String convertStreamToString(InputStream is) { //  this thing convers
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

    private String get(String url) {

        HttpClient httpclient = new DefaultHttpClient();
        try{
            HttpGet httpget = new HttpGet(url);
            httpget.addHeader("Authorization", this.getAuthstring());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if(entity != null){
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                Log.d("Httpio", result );
                json = result;
                instream.close();
                return json;
            }
        }

        catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }

    public String post(String url){

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        if (this.getAuthstring().length() > 2){ // still dont like magic numbers
            httpPost.addHeader("Authorization",authstring);
            Log.d("Httpio authstring",authstring);
        }

        try{

            httpPost.setEntity(new UrlEncodedFormEntity(this.data));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if(entity != null){
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                Log.d("httipio result",result);
                json = result;

                return json;
            }

            return json;
        }
        catch (ClientProtocolException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        Message msg = new Message();
        msg.obj=json;
        this.httphandler.sendMessage(msg);
    }

}
