package com.netchosis.somthing.project2_phone2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class editprofile extends Activity  {
    public final static String C_USER = "com.example.project2.CUSER";
    private EditText firstname_box;
    private EditText lastname_box;
    private EditText username_box;
    private EditText email_box;
    private EditText phone_box;
    private EditText profession_box;
    private EditText bio_box;
    private Spinner gender_box;
    private CheckBox accept_calls;
    private static user currentuser;

    final Handler httphandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String Data = (String) msg.obj;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent  intent = getIntent();
        Bundle cu = intent.getExtras();
        Log.d("Edit Profile","were on the edit profile screen!");

        try{
            currentuser = cu.getParcelable(Getusers.CURRENT_USER);
            setCurrentuser(currentuser); // Really not sure why this needs to happen
            Log.d("EDITPROFILE CREATED", currentuser.getUsername());
            setContentView(R.layout.fragment_editprofile);
            populate_profile();
        }
        catch (NullPointerException e)
        {
            Log.d("Edit profile"," current user came back null we cant edit the users profile");
        }
    }

    public void setCurrentuser(user currentuser) {this.currentuser = currentuser;}

    protected void populate_profile() {
        firstname_box = (EditText) findViewById(R.id.fnamebox1);
        lastname_box = (EditText) findViewById(R.id.lnamebox);
        phone_box = (EditText) findViewById(R.id.phonebox);
        bio_box = (EditText) findViewById(R.id.bio1);
        email_box = (EditText) findViewById(R.id.email_box);
        gender_box = (Spinner) findViewById(R.id.Gender);

        try {
            firstname_box.setText(currentuser.getFirstname());
        } catch (NullPointerException e) {
            firstname_box.setHint("first name");
            Log.d("Populate profile","first name box");
        }

        try {
            lastname_box.setText(currentuser.getLastname());
        } catch (NullPointerException e) {
            lastname_box.setHint("last name");
        }

        try {
            phone_box.setText(currentuser.getFirstname());
        } catch (NullPointerException e) {
            phone_box.setHint("Phone: eg 510-555-5882");
        }

        try {
            bio_box.setText(currentuser.getBio());
        } catch (NullPointerException e) {
            phone_box.setHint("say somthing about yourself");
        }

        try {
            email_box.setText(currentuser.getEmail());
        } catch (NullPointerException e) {
            email_box.setHint("email address");
        }

    }
    public void Save(View view){ // happends when save button is pressed
        firstname_box = (EditText) findViewById(R.id.fnamebox1);
        lastname_box = (EditText) findViewById(R.id.lnamebox);
        phone_box = (EditText) findViewById(R.id.phonebox);
        bio_box = (EditText) findViewById(R.id.bio1);
        gender_box = (Spinner) findViewById(R.id.Gender);

        currentuser.setGender(gender_box.getSelectedItem().toString());
        Log.d("gender check",currentuser.getGender());
        currentuser.setFirstname(firstname_box.getText().toString());
        currentuser.setLastname(lastname_box.getText().toString());
        currentuser.setPhone(phone_box.getText().toString());
        currentuser.setBio(bio_box.getText().toString());


        currentuser.setEmail(email_box.getText().toString());
      //  Log.d("CURRENTFUCK",currentuser.getAuth());
     //  this.currentuser.setBio(bio_box.getText().toString());
        update_user(currentuser);
    }

    // Need to figure out what to do about profession and intrests
    public void update_user(user User){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);

        nameValuePairs.add(new BasicNameValuePair("firstname",User.getFirstname()));
        nameValuePairs.add(new BasicNameValuePair("lastname",User.getLastname()));
        nameValuePairs.add(new BasicNameValuePair("phone",User.getPhone()));
        nameValuePairs.add(new BasicNameValuePair("gender",User.getGender()));

        nameValuePairs.add(new BasicNameValuePair("aboutyou",User.getBio()));
        nameValuePairs.add(new BasicNameValuePair("email",User.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("timezone","pacific"));

        httpio userupdate = new httpio(httphandler);

        userupdate.setPost();
        userupdate.setAuthstring(currentuser.getAuth());
        userupdate.sendData(nameValuePairs);
        userupdate.execute("http://10.0.255.3/update_user/");
    }
 /*
  protected void intrests(View view){
        Intent edit_intrestintent = new Intent(this, Intrestactivity.class);
        edit_intrestintent.putExtra(C_USER,currentuser);
        edit_intrestintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(edit_intrestintent);
    }
/*
    protected void Schedule(View view){
  //      Intent edit_schedule = new Intent(this,);
        edit_schedule.putExtra(C_USER,currentuser);
        edit_schedule.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(edit_schedule);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.usermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }


    }

}
