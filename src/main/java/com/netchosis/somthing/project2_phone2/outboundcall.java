package com.netchosis.somthing.project2_phone2;

import android.app.Activity;
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
    public SipAudioCall mycall;
    public TextView calle;

    SipAudioCall.Listener listener = new SipAudioCall.Listener() {
        @Override
        public void onCallEstablished(SipAudioCall call){
            call.startAudio();
        }
        @Override
        public void onCallEnded(SipAudioCall call){
            //idk
        }
    };

    SipSession.Listener slistner = new SipSession.Listener(){

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outboundcall_activity);
    //    calle = (TextView) findViewById(R.id.called_user);
     //   calle.setText(UserProfile.getClickeduser().getFirstname() +" "+ UserProfile.getClickeduser().getLastname());
        try {
            startcall();
        } catch (SipException e) {
            e.printStackTrace();
        }

    }

    public void startcall() throws SipException{
       try{
            String Uri = Sipwork.sipprofile.getUriString();
            String to_uri = UserProfile.getClickeduser().sipuri;
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
       mycall.endCall();
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
