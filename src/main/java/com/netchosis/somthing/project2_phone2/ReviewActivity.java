package com.netchosis.somthing.project2_phone2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ReviewActivity extends ActionBarActivity {
    protected user review_user;
    private String fullname;
    private TextView display_text;
    private String SALUTATION_TEXT = "Let us know how your conversation went";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        Intent intent = getIntent();
        Bundle cu = intent.getExtras();

        this.review_user = cu.getParcelable(outboundcall.EXTRA_USER);
        this.fullname = fullname(this.review_user);
        this.display_text = (TextView) findViewById(R.id.salutation);
        this.display_text.setText(SALUTATION_TEXT + this.fullname);

    }

    private String fullname(user user){
        String Firstname = user.getFirstname();
        String Lastname = user.getLastname();
        String Fullname = Firstname + " " + Lastname;
        return Fullname;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
