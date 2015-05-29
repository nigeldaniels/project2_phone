package com.netchosis.somthing.project2_phone2;

/**
 * Created by nigel on 4/22/15.
 */
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class Sipwork extends Service implements Runnable {

    public String message;
    public IncomingCallReceiver callReceiver;
    private String SIP_DOMAIN = "sip.netchosis.com";
    private String SIP_PROXY = "sip.netchosis.com";
    public static final int SIP_STATE_REGISTERING = 10;
    public static final int SIP_STATE_REGFAILED  = 11;
    public static final int SIP_STATE_REGISTERD = 99;
    private String SIP_USER;
    private String SIP_PASSWORD;
    private String webcreds;
    public Context context = getBaseContext();
    private JSONObject sipcreds;
    private JSONObject json;
    private JSONObject jsonObject;
    public static SipManager sipman = null;

    public SipManager getSipman() {
        return sipman;
    }

    public static SipProfile sipprofile = null;

    private JSONObject Getsipcreds(String url) throws IOException, JSONException {

        HttpClient httpclient = new DefaultHttpClient();

        try{
            HttpGet httpget = new HttpGet(url);
            Log.d("webcreds!", webcreds);
            httpget.addHeader("Authorization", "Token " + webcreds);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null){
                InputStream instream = entity.getContent();
                String result = Settings.convertStreamToString(instream);
                Log.d("result",result);
                json = new JSONObject(result);
                instream.close();

                return json;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }
    // Takes sip creds as a json object and commits them to shared preferances
    private void storecreds(JSONObject sipcreds) throws JSONException{
        SharedPreferences settings = context.getSharedPreferences("sipshit", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", sipcreds.getString("username"));
        editor.putString("password", sipcreds.getString("password"));
        editor.commit();
    }

    public void setup_incoming(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("example.project2.INCOMING_CALL");
        callReceiver = new IncomingCallReceiver();
        registerReceiver(callReceiver, filter);
    }

    // Takes sip creds and  returns a sip profile object
    private SipProfile buildsip(JSONObject sipcreds ) throws JSONException, ParseException{

        SIP_USER = sipcreds.getString("username");
        SIP_PASSWORD = sipcreds.getString("password");

        if (sipman == null){
            sipman = SipManager.newInstance(getBaseContext());
        }

        SipProfile.Builder builder = new SipProfile.Builder(SIP_USER, SIP_DOMAIN);
        builder.setPassword(SIP_PASSWORD);
        builder.setAuthUserName(sipcreds.getString("username"));
        builder.setAutoRegistration(true);
        builder.setOutboundProxy(SIP_DOMAIN);
        builder.setProtocol("UDP");

        SipProfile mSipProfile = builder.build();
        sipint();

        if (mSipProfile == null) {
            Log.d("problem", "that seems to be the problem");
        }
        return mSipProfile;
    }

    public void sipint(){

    }

    //Creates a sip managger from
    public void sipint(SipProfile sipprofile) throws SipException{

        Intent intent = new Intent();
        intent.setAction("com.netchosis.somthing.project2_phone2.INCOMING_CALL");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, Intent.FILL_IN_DATA);
        sipman.open(sipprofile, pendingIntent, null);
        sipman.register(sipprofile,10,null);

        if (sipman == null){
            Log.d("problem", "found the null");
        }

    }

    void handleState(Getusers sipusers,int state){
        sipusers.HandleSIP(this, state);
    }

    private void setlisten() throws SipException{

        sipman.setRegistrationListener(sipprofile.getUriString(), new SipRegistrationListener() {

                    public void onRegistering(String localProfileUri) {
                        Getusers sipusers = Getusers.getInstance();
                        int state = SIP_STATE_REGISTERING;
                        handleState(sipusers, state);
                    }

                    public void onRegistrationDone(String localProfileUri, long expiryTime) {
                        Getusers sipusers = Getusers.getInstance();
                        int state = SIP_STATE_REGISTERD;
                        handleState(sipusers,state);
                    }

                    public void onRegistrationFailed(String localProfileUri, int errorCode,String errorMessage) {
                        Log.d("errorMessage",errorMessage + localProfileUri);
                        Getusers sipusers = Getusers.getInstance();
                        int state = SIP_STATE_REGFAILED;
                        handleState(sipusers,state);
                    }
                }
        );
    }

    public void run(){
        try {
            Looper.prepare();
            Log.d("THREAD","thread started");
            //setup_incoming();
            sipcreds = Getsipcreds("http://10.0.255.3/sipusers/");
            Log.d("sipcreds",sipcreds.toString());
            //storecreds(sipcreds);
            sipprofile = buildsip(sipcreds);

            sipint(sipprofile);
            setlisten();
            setup_incoming();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("broken", e.toString());
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SipException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }
// The fallowing methods are Service specific

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        try{
            this.webcreds = intent.getStringExtra(Getusers.EXTRA_MSG);
            Log.d("onstart:", webcreds);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        Thread sip = new Thread(this);
        sip.start();
        return START_STICKY;

    }

    @Override
    public void onCreate() {


    }


}
