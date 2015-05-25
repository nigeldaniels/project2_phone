package com.netchosis.somthing.project2_phone2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

/**
 * Created by nigel on 5/25/15.
 */
public class Intrests extends ListActivity {


    public ListView listView;
    private static user currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle cu = intent.getExtras();
        Log.d("INTRESTS", "where on the intrest screen");

        try{
            currentuser = cu.getParcelable(Getusers.CURRENT_USER);
            Log.d("EDITPROFILE CREATED", currentuser.getUsername());
            buildmenu();

        }
        catch (NullPointerException e)
        {
            Log.d("INTRESTS:"," current user came back null we cant edit the users profile");
        }
    }

    public void buildmenu(){
        netdata getgoing = new netdata();


    }



}
