package com.netchosis.somthing.project2_phone2;

import android.app.ListActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by nigel on 4/22/15.
 */

public class Getusers extends ListActivity {
    private static final Getusers Instance = new Getusers();

    public final static String EXTRA_USER = "com.netchosis.somthing.project2_phone2.USER";
    public final static String EXTRA_MSG ="com.netchosis.somthing.project2_phone2.MSG";
    public final static String CURRENT_USER="com.netchosis.somthing.project2_phone2.CURRENTUSER";
    public String perfs ="perfs";
    public ListView listview;
    private  ArrayList<Object> dataarray;
    private String ident = null;
    public Handler mHandler;
    public String message;
    public SharedPreferences.Editor editor;

    public void setCurrentuser(user currentuser) {
        this.currentuser = currentuser;
    }

    public user currentuser = new user();

    String url = "http://10.0.255.3/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(perfs,MODE_PRIVATE);

        Intent intent = getIntent();
        this.message = intent.getStringExtra(Authinticate.EXTRA_TOKEN); // message will be null if activity is started by a new signup
        Toast.makeText(getApplicationContext(), this.message, Toast.LENGTH_SHORT).show();
        String Message2 = intent.getStringExtra(Signup.EXTRA_TOKEN); //Message 2 will be null unless this activity is started by a new signup

        if ( this.message == null){
            this.message = Message2;
            editor = store(settings,message);// stores token in shared preferances file called perfs
        }

        else {
            editor = store(settings,message);
        }

        whoami(); // when the response to whoami comes back we call buildmenu in handleMessage

        Intent sintent = new Intent(this,Sipwork.class); //intent for the sip background service
        Log.d("Getusers:", message);
        sintent.putExtra(EXTRA_MSG, message);
        startService(sintent); // this starts the sip background service.
    }

    public void whoami() {
        List<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>(2);
        //nameValuePairs.add(new BasicNameValuePair("item1","yes"));
        Httpio whoami = new Httpio(httpgethandler);
        whoami.setPost();
        whoami.setAuthstring(message);
        whoami.sendData(nameValuePairs);
        whoami.execute("http://10.0.255.3/whoami/");
    }

    final Handler httpgethandler = new Handler(){
        @Override
        public void handleMessage(Message msg){

            String Data = (String) msg.obj;
            try{
                Toast.makeText(getApplicationContext(), "handle message is being called",Toast.LENGTH_SHORT).show();
                JSONObject json = new JSONObject(Data);
                String user = json.get("item1").toString();
                Log.d("You are", user);
                setIdent(user);
                buildmenu();
                Getimages images = new Getimages(dataarray);
                images.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    };

    final Handler httphandler = new Handler(); // this es nothing its only here because Httpio expects a handler

    public SharedPreferences.Editor store (SharedPreferences settings, String Token) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Token",Token);
        editor.commit();

        return editor;
    }

    public void setIdent(String ident){
        this.ident = ident;
        Log.d("IDENT:",this.ident);
    }


    public void HandleSIP (Sipwork sipWork, int state){
        switch (state) {
            case Sipwork.SIP_STATE_REGISTERD:
                Message RegisterdMessage = mHandler.obtainMessage(state,sipWork);
                RegisterdMessage.sendToTarget();
                break;

            case Sipwork.SIP_STATE_REGISTERING:
                Message RegisteringMessage = mHandler.obtainMessage(state,sipWork);
                RegisteringMessage.sendToTarget();
                break;

            case Sipwork.SIP_STATE_REGFAILED:
                Message FailedMessage = mHandler.obtainMessage(state,sipWork);
                FailedMessage.sendToTarget();
                break;

        }
    }
    public static Getusers getInstance() {
        return Instance;
    }

    public ArrayList<Object> parsedata(JSONArray jsondata){
        Log.d("balls",jsondata.toString());
        ArrayList<Object> userobjects = new ArrayList<Object>(jsondata.length());
        for (int i = 0; i < jsondata.length(); i++) {
            try{

                JSONObject jsonObject =	jsondata.getJSONObject(i);
                user User = new user();

                User.setUsername(jsonObject.getString("username"));
                //Log.d("jsonUSERNAME:",jsonObject.getString("username"));
                User.setImgurl(jsonObject.getString("Image"));
                User.setEmail(jsonObject.getString("username"));
                User.setId(jsonObject.getString("id"));
                User.setTimezone(jsonObject.getString("timezone"));
                User.setPhone(jsonObject.getString("phonenumber"));
                User.setProfession(jsonObject.getString("profession"));
                User.setBio(jsonObject.getString("aboutyou"));
                User.setGender(jsonObject.getString("gender"));
                User.setAge(jsonObject.getString("age"));
                User.setStatus(jsonObject.getString("status"));
                User.setFirstname(jsonObject.getString("first_name"));
                User.setLastname(jsonObject.getString("last_name"));

                if (User.getId().equals(this.ident)){
                    setCurrentuser(User);
                    User.setAuth(message);
                    Log.d("Current USER:",User.getUsername());
                }

                userobjects.add(User);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return userobjects;
    }
    // this takes your object array and turns it into a list of usernames
    public ArrayList<String> usernames(ArrayList<Object> dataarray) {
        ArrayList <String> userlist = new ArrayList<String>(dataarray.size());
        for (int i = 0; i < dataarray.size(); i++){
            user User = (user) dataarray.get(i);
            String username = User.getFirstname();
            String lastname = User.getLastname();
            if  (User.getLastname().length() < 1){   // this is fucked even  for me ! FIX FIX FIX
                userlist.add(User.getUsername());
                Log.d("shit",User.getLastname());
            }
            else{
                Log.d("Lastname",User.getLastname());
                userlist.add(username + " " + lastname);
            }

        }
        return userlist;
    }
    public int testnetwork(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if ( networkInfo != null && networkInfo.isConnected()){
            return 0;
        }

        else {
            return 1;
        }
    }

    public void buildmenu(){
        netdata getgoing = new netdata(); // This Goes out and gets the users
        getgoing.setToken(message);
        getgoing.execute(url);

        try {
            JSONArray jsondata = getgoing.get();
            Log.d("IS THIS EMPTY ?", String.valueOf(jsondata));
            listview = getListView();

            dataarray = parsedata(jsondata);
            Log.d("shit",dataarray.toString());
            ArrayList<String> userlist = usernames(dataarray);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userlist);
            listview.setAdapter(adapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        user clickedUser = (user) dataarray.get(position);
        Intent profileintent = new Intent(this, UserProfile.class);
        profileintent.putExtra(EXTRA_USER, clickedUser);

        profileintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(profileintent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usermenu, menu);
        return true;
    }

    public void editprofile(user Currentuser){ // Notice that we set the current user
        Log.d("edit profile activity:","other");
        Intent editprofileintent = new Intent(this, Editprofile.class);
        editprofileintent.putExtra(CURRENT_USER, Currentuser);
        editprofileintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(editprofileintent);
    }

    public void logout(){
        SharedPreferences settings = getSharedPreferences(perfs,MODE_PRIVATE);
        String tokencheck = settings.getString("Token", "empty");
        if (tokencheck.length() < 5 || tokencheck == "empty"){ // this is pure fucking shit
            Log.d("WE HAVE A PRBOLEM",tokencheck);
        }
        else {
            editor.clear();
            editor.commit();
            System.exit(0);
        }
    }

    public void avalible(){ // marks this user as avalible to recive incomeing calls
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("item1","yes"));
        Httpio setavalible = new Httpio(httphandler);
        setavalible.setPost();
        setavalible.setAuthstring(message);
        setavalible.sendData(nameValuePairs);
        setavalible.execute("http://10.0.255.3/set_status/");
    }

    public boolean isitme(){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.profile:
                editprofile(this.currentuser);
                return true;
            case R.id.logout:
                logout();
                return true;
            case R.id.Avalible_for_calls:
                avalible();
                item.setTitle("Make Unavalible");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



