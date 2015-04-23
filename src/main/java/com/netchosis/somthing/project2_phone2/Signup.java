package com.netchosis.somthing.project2_phone2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nigel on 4/22/15.
 */
public class Signup extends Activity {
    public final static String EXTRA_TOKEN = "com.example.project2.TOKEN2";
    public EditText ident;
    public EditText password;
    public String url = "http://sip.netchosis.com/api-signup/";
    public Message msg;
    private String timezone = "pacific";
    private String s_ident;
    private String s_password;
    private JSONStringer json = new JSONStringer();
    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mcontext = getApplicationContext();
        ident = (EditText) findViewById(R.id.ident);
        password  = (EditText) findViewById(R.id.password);
    }

    public void buttonclick(View view) throws JSONException {

        s_ident = ident.getText().toString();
        s_password = password.getText().toString();

        List<NameValuePair>nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair("username",s_ident));
        nameValuePairs.add(new BasicNameValuePair("password",s_password));
        nameValuePairs.add(new BasicNameValuePair("timezone",timezone));

        httpio httpsend = new httpio(httphandler);
        httpsend.setPost(); // this tells httpio we are posting
        httpsend.sendData(nameValuePairs);
        httpsend.execute(url);
    }

    final Handler httphandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d("error",msg.toString());
            String Data = (String) msg.obj;

            try {
                JSONObject json = new JSONObject(Data);
                String token = json.get("item1").toString();
                Log.d("Signup Token:", token);
                getUsers(token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void getUsers(String token){
        Intent intent = new Intent(mcontext, Getusers.class);
        intent.putExtra(EXTRA_TOKEN, token);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcontext.startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
