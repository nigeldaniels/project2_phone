package com.netchosis.somthing.project2_phone2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nigel on 5/30/15.
 */
public class uitools {

    private ArrayList<Object> dataarray;
    private JSONArray jasondata;

    public void setJasondata(JSONArray jasondata) {
        this.jasondata = jasondata;
    }


    public JSONArray getJasondata() {
        return jasondata;
    }

    public void setDataarray(ArrayList<Object> dataarray){
        this.dataarray = dataarray;
    }


    public ArrayList<Object> parsedata_intrests(JSONArray jsondata){
        ArrayList<Object> intrests = new ArrayList<Object>(jsondata.length());
        for (int i = 0; i < jsondata.length(); i++) {
            try {
                JSONObject jsonObject = jsondata.getJSONObject(i);
                intr
            }
        }


    }



}
