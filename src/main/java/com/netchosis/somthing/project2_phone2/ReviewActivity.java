package com.netchosis.somthing.project2_phone2;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import android.view.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class ReviewActivity extends ActionBarActivity {
    final android.os.Handler httphandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg){
            String Data = (String) msg.obj;
        }
    };

    protected user review_user;
    private String fullname;
    private TextView display_text;
    private String SALUTATION_TEXT = "Let us know how your conversation went with!";
    private RatingBar ratingBar;
    private String comments;
    private EditText commentbox;
    private int rateing = -1;

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

        addListenerOnRatingBar();
    }

    public void addListenerOnRatingBar(){
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.getOnRatingBarChangeListener();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                checkrating(ratingBar);
            }
        });

    }

    public void review_button(View view){
         send_review();

    }

    private float checkrating(RatingBar ratingBar) {
        if (ratingBar.getNumStars() < 3){
            this.rateing = (int) ratingBar.getRating();
            return 0;
        }
        else {
            this.rateing = (int) ratingBar.getRating();
            return ratingBar.getRating();
        }
    }

    private boolean send_review(){
        get_comments();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
        nameValuePairs.add(new BasicNameValuePair("reviewer",Valuestore.get_current_user(getApplicationContext())));
        nameValuePairs.add(new BasicNameValuePair("reviewduser",this.review_user.getId()));
        nameValuePairs.add(new BasicNameValuePair("stars",String.valueOf(this.rateing)));
        nameValuePairs.add(new BasicNameValuePair("content",this.comments));

        Httpio reviewsender = new Httpio(httphandler);
        reviewsender.setPost();
        reviewsender.setAuthstring(Valuestore.getToken(getApplicationContext()));
        reviewsender.sendData(nameValuePairs);
        reviewsender.execute(Urls.REVIEW_URL);
        return true;
    }

    private void get_comments(){
        this.commentbox = (EditText) findViewById(R.id.commentbox);
        this.comments = this.commentbox.getText().toString();
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
