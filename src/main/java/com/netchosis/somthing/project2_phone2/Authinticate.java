package com.netchosis.somthing.project2_phone2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;

/**
 * Created by nigel on 4/22/15.
 */
public class Authinticate extends AsyncTask<String, Void, String> {
    public final static String EXTRA_TOKEN = "com.example.project2.TOKEN";
    public final static String EXTRA_USER_ID = "com.example.project2.USERID";
    public Context mcontext;
    public String M_email;
    public String M_password;

    public void setToken(String token) {
        this.token = token;
    }

    public String token;
    public boolean M_Authed;
    public byte[] buffer;

    public Authinticate(Context context){
        mcontext = context;
    }

    public void setM_email(String m_email) {
        M_email = m_email;
    }
    public void setM_password(String m_password) {
        M_password = m_password;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            if (this.M_email == "n0pe"){
                return quicktoken();
            }
            else {
                return connect(urls[0]);
            }

        } catch (IOException e) {
            return "url is not right";
        }
    }

    protected String quicktoken(){
        return "quick and cool";
    }

    protected void onPostExecute(String result) {        //this starts the Getusers activity
        try {
            if (validatetoken(token)) {
                Intent intent = new Intent(mcontext, Getusers.class);
                intent.putExtra(EXTRA_TOKEN, token);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }

        } catch (NullPointerException e) {
            Toast.makeText(mcontext, "Incorrect username or password", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validatetoken(String token){
        if (token.isEmpty()){ return false;}
        else{
            return true;
        }

    }

    private String connect(String url) throws IOException { //connect is where the work happends
        url = "http://sip.netchosis.com/api-token/";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpPost httppost = new HttpPost (url);

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", M_email));
            nameValuePairs.add(new BasicNameValuePair("password", M_password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            Log.d("Authinticate Username and password name value", nameValuePairs.toString());

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            Log.i("preada", response.getStatusLine().toString());

            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = Settings.convertStreamToString(instream);
                JSONObject json = new JSONObject(result);
                try {
                    token = json.getString("token");
                    Log.d("Authinticate token", token);
                    instream.close();
                }
                catch (NullPointerException e)  {
                    e.printStackTrace();
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        catch (ClientProtocolException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        catch (JSONException e) {
            Log.d("JSON ERROR",e.getMessage());
            e.printStackTrace();
        }
        return "cool";
    }

}
