package com.netchosis.somthing.project2_phone2;

import android.app.Activity;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class outboundcall extends Activity {
    public final static String EXTRA_USER = "outboundcall_extras";
    public SipAudioCall mycall;
    public TextView calle;
    protected user currentUser;

    SipAudioCall.Listener listener = new SipAudioCall.Listener() {
        @Override
        public void onCallEstablished(SipAudioCall call){
            Log.d("Callestablished", "call started");
            call.startAudio();

        }
        @Override
        public void onCallEnded(SipAudioCall call){
            try {
                call.endCall();
            } catch (SipException e) {
                e.printStackTrace();
            }
        }
    };

    SipSession.Listener slistner = new SipSession.Listener(){

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outboundcall_activity);
        calle = (TextView) findViewById(R.id.callee);
        Intent intent = getIntent();
        Bundle cu = intent.getExtras();
        this.currentUser = cu.getParcelable(UserProfile.EXTRA_USER);
        calle.setText(this.currentUser.getFirstname() +" "+ this.currentUser.getLastname());
        setTitle(currentUser.getFirstname());

        try {
            startcall();
        } catch (SipException e) {
            e.printStackTrace();
        }

    }

    public void startcall() throws SipException{
       try{
            String Uri = Sipwork.sipprofile.getUriString();
            String to_uri = this.currentUser.sipuri;
            mycall = Sipwork.sipman.makeAudioCall(Uri, to_uri, listener, 30);
        } catch (NullPointerException e) {
            Log.d("ERR",e.toString());
        }

        try{

            Sipwork.sipman.createSipSession(Sipwork.sipprofile, slistner);
        } catch (NullPointerException e) {
           Log.d("slistner",slistner.toString());
           Log.d("sipprofile:", Sipwork.sipprofile.toString());

            e.printStackTrace();
        }

    }

    public void endcall(View view) throws SipException { //button
//       mycall.endCall(); /// remember to turn this back on
       Intent reviewintent = new Intent(this,ReviewActivity.class);
       reviewintent.putExtra(EXTRA_USER,this.currentUser);
       startActivity(reviewintent);
       this.finish();
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_outboundcall2, container, false);
            return rootView;
        }
    }

 }
