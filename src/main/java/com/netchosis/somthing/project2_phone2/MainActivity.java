package com.netchosis.somthing.project2_phone2;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.view.*;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_TOKEN = "com.example.project2.TOKEN";
    private static final String DEBUG_TAG = "HTTP";
    private EditText email;
    private EditText password;
    private TextView textview;
    private String s_email;
    private String s_password;
    private String perfs = "perfs";
    private Spinner Spinner1;
    public String token;
    public String stoken = null;
    public Context maincontext;
    public byte[] buffer = new byte[40];
    Settings msettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maincontext = getApplicationContext();

        this.email = (EditText) findViewById(R.id.Email);
        this.password = (EditText) findViewById(R.id.Password);
    }

    protected void onStart(){
        super.onStart();
        maincontext = getApplicationContext();
        SharedPreferences settings = getSharedPreferences(perfs,MODE_PRIVATE);
    }

    public void buttonclick(View view) {  // TTHIS IS WHATS HAPPENING WHEN U CLICK THE BUTTON
        String stringUrl = Urls.API_TOKEN_URL;

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            this.s_email =  email.getText().toString();
            this.s_password = password.getText().toString();
            if ( this.s_email != null && !this.s_email.isEmpty() && this.s_password != null && !this.s_password.isEmpty()){ // this checks to make sure email and password are filled out
                Authinticate Authinticater = new Authinticate(maincontext);
                Authinticater.setM_email(s_email);
                Authinticater.setM_password(s_password);
                Authinticater.execute(stringUrl);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please Provide a user name and password", Toast.LENGTH_LONG);
            }
        }

        else {
            Toast.makeText(getApplicationContext(), "The network is fucked up ",Toast.LENGTH_SHORT).show();
        }

   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
