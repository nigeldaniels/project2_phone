package com.netchosis.somthing.project2_phone2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import android.util.Log;

/**
 * Created by nigel on 4/22/15.
 */
public class IncomingCallReceiver extends BroadcastReceiver {
    public final static String EXTRA_INFO = "com.example.project2.USER_INFO";
    public Intent intent = null;
    public String displayname;
    public static SipAudioCall incomingCall = null;
    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            SipAudioCall.Listener listener = new  SipAudioCall.Listener(){
                @Override
                public void onRinging(SipAudioCall call, SipProfile caller) {
                    try{
                        Log.d("ring","ading ding ding dong");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            };

            incomingCall = Sipwork.sipman.takeAudioCall(intent,listener);

        }

        catch (Exception e){
            e.printStackTrace();
        }
        intent.setClass(context, IncomingCall.class);
        intent.putExtra(EXTRA_INFO, displayname);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
