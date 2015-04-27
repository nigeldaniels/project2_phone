package com.netchosis.somthing.project2_phone2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nigel on 4/26/15.
 */
public class UserCreator implements Parcelable.Creator<user> {
    @Override
    public user createFromParcel(Parcel arg0) {
        return new user(arg0);
    }
    @Override
    public user[] newArray(int size) {
        return new user[size];
    }
}
