package com.netchosis.somthing.project2_phone2;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nigel on 5/27/15.
 */
public class valuestore {
    private static final String PREF_NAME ="perfs" ;

    private SharedPreferences sharedPreferences;


    public valuestore(){

    }

    private static SharedPreferences getPerfs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getToken(Context context){
        return getPerfs(context).getString("token","FUCKED");
    }

    public static void setToken(Context context, String input){
        SharedPreferences.Editor editor = getPerfs(context).edit();
        editor.putString("token",input);
        editor.commit();
    }

}
