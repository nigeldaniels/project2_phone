package com.netchosis.somthing.project2_phone2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by nigel on 5/25/15.
 */
public class Intrests extends ListActivity {

    public ListView listView;
    private static user currentuser;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle cu = intent.getExtras();

        Log.d("INTRESTS", "we are on the intrest screen");

        try {
            currentuser = cu.getParcelable(Getusers.CURRENT_USER);
            Log.d("INTRESTS CREATED", currentuser.getUsername());
            buildmenu();


        }

        catch (NullPointerException e)
        {
            Log.d("INTRESTS:"," current user came back null we cant edit the users profile");
        }
    }

    public void buildmenu() {
        String url = "http://10.0.255.3/listusers";
        token = Valuestore.getToken(getApplicationContext());
        netdata intrests = new netdata();
        intrests.setToken(token);
        intrests.execute(url);
        try {
            JSONArray json = intrests.get();
            listView = getListView();
            dataarray = parsedata(json);
            Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_SHORT).show();


        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String>


}
