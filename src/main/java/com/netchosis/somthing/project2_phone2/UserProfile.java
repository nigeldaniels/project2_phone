package com.netchosis.somthing.project2_phone2;

import android.app.Activity;
import android.content.Intent;
import android.net.sip.SipException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfile extends Activity {

    public final static String EXTRA_USER = "shit";
	private TextView name;
	private TextView age;
	private TextView gender;
	private TextView location;
	private TextView bio;
	private TextView profession;
	private TextView status;
	private ImageView pic;
    private user clickeduser;

    /*public static user getClickeduser() {
        return clickeduser;
    }

    public void setClickeduser(user clickeduser){
        this.clickeduser = clickeduser;
    }
    */

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle cu = intent.getExtras();

        clickeduser = cu.getParcelable(Getusers.EXTRA_USER);
	    //setClickeduser(clickeduser); // WTF WAS I DOING

        setTitle(clickeduser.getFirstname());

		setContentView(R.layout.activity_user_profile);
		name = (TextView) findViewById(R.id.name);
		age  = (TextView) findViewById(R.id.Age);
		//gender = (TextView) findViewById(R.id.Gender);
		profession = (TextView) findViewById(R.id.Profession);
		bio = (TextView) findViewById(R.id.bio);
		location = (TextView) findViewById(R.id.Location);
		status = (TextView) findViewById(R.id.Status);
		pic = (ImageView) findViewById(R.id.callee);
		name.setText(clickeduser.getFirstname() + " " + clickeduser.getLastname());
		age.setText(clickeduser.getAge());
		profession.setText(clickeduser.getProfession());
		bio.setText(clickeduser.getBio());
		location.setText(clickeduser.getLocation());
		//gender.setText(clickeduser.getGender());
		status.setText(clickeduser.getStatus());
	//	pic.setImageBitmap(Getimages.getInstance().getimage(clickeduser.getId()));
	}

    public void calling(){ //TODO make a calling activity
        Intent callingintent = new Intent(this, outboundcall.class);
        callingintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callingintent.putExtra(EXTRA_USER, this.clickeduser);
        startActivity(callingintent);
    }
	
	public void callbutton(View view) throws SipException{
	 calling();

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}

}
