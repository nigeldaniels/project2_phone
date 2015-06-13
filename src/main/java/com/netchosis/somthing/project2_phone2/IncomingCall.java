package com.netchosis.somthing.project2_phone2;

import android.app.Activity;
import android.content.Intent;
import android.media.AsyncPlayer;
import android.net.sip.SipException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class IncomingCall extends Activity {
	
	private TextView caller;
	private String calle;
    public Intent intent = getIntent();
	public AsyncPlayer player = new AsyncPlayer("ring");
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incoming_call1);
		Intent intent = getIntent();
		calle = intent.getStringExtra(IncomingCallReceiver.EXTRA_INFO);
		caller = (TextView) findViewById(R.id.caller);
        caller.setText(calle);
        ring(player);
	}

    public void ring(AsyncPlayer player){

        player.play(getApplicationContext(), android.provider.Settings.System.DEFAULT_RINGTONE_URI, true, 2);
    }

    public void stopring(AsyncPlayer player){
        player.stop();
    }
	public void Answerbutton(View view){
        stopring(player);
        try{
            IncomingCallReceiver.incomingCall.answerCall(0);
            IncomingCallReceiver.incomingCall.startAudio();
        } catch (SipException e) {
            e.printStackTrace();
        }

    }

    public void Ignore(View view){
        stopring(player);
        try{
            IncomingCallReceiver.incomingCall.endCall();

        } catch (SipException e) {
            e.printStackTrace();
        }
        this.finish();
    }


   public void Hangup(View view) throws SipException {
       stopring(player);
       if (IncomingCallReceiver.incomingCall.isInCall()){
            IncomingCallReceiver.incomingCall.endCall();
       }

       else{
           Log.d("Hangup", "hangup button was used");
       }
       this.finish();
   }

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.incoming_call, menu);
		return true;
	}
}


