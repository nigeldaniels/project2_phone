package com.netchosis.somthing.project2_phone2;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
/**
 * Created by nigel on 4/22/15.
 */
public class user implements Parcelable {
    public static finalocal Parcelable.Creator CREATOR = new UserCreator();
    private String EMPTY_FIELD = "empty_field";
    public String username;
    public String firstname;
    public String lastname;
    public String id;
    public String timezone;
    public String language;
    public String profession;
    public String age;
    public String location;
    public String bio ;
    public String gender;
    public String status;
    public String sipuri;
    public String imgurl;
    public String urlprefix = "http://sip.netchosis.com/images/";
    public String phone;
    public String email;
    public String auth;

    public user(Parcel source) {
        username = source.readString();
        firstname = source.readString();
        lastname = source.readString();
        id = source.readString();
        timezone = source.readString();
        language = source.readString();
        profession = source.readString();
        age = source.readString();
        location = source.readString();
        bio = source.readString();
        gender = source.readString();
        status = source.readString();
        imgurl = source.readString();
        email = source.readString();
        phone = source.readString();
        auth = source.readString();

        try{
            sipuri = username.replaceAll("@", "-");
        }
        catch (NullPointerException e){
            sipuri = username +"-"+"nousername.com";

        }
        sipuri = sipuri+"@sip.netchosis.com";
    }
    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = urlprefix+ imgurl+".phone.jpg";
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public user() {
        // TODO Auto-generated constructor stub
    }
    public String getAuth(){
        Log.d("getauth",auth);
        return auth;
    }
    public void setAuth(String Auth){
        Log.d("setauth:",Auth);
        this.auth = Auth;
        Log.d("this.auth", this.auth);
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getBio() {

        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getFirstname() {
        if (this.firstname != null){
            return firstname;
        }
        else {
            return "firname issue";
        }
    }
    public String getEmail () {
        return this.email;
    }
    public String getPhone(){

        return phone;
    }
    public String getsupuri(){
        return sipuri;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        if (this.lastname != null){
            return this.lastname;
        }
        else{
            return "lastname issue";
        }

    }
    public void setLastname(String lastname) {

        this.lastname = lastname;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    public String getLanguage() {
        return language;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeString(username);
        arg0.writeString(firstname);
        arg0.writeString(lastname);
        arg0.writeString(id);
        arg0.writeString(timezone);
        arg0.writeString(language);
        arg0.writeString(profession);
        arg0.writeString(age);
        arg0.writeString(location);
        arg0.writeString(bio);
        arg0.writeString(gender);
        arg0.writeString(status);
        arg0.writeString(imgurl);
        arg0.writeString(email);
        arg0.writeString(phone);
        arg0.writeString(auth);

    }

}
