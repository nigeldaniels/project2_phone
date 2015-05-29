package com.netchosis.somthing.project2_phone2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public  class Getimages extends Thread {
    //private static final Getimages Instance = new Getimages();
	
	public String url;
	public String token;
	public String id;
    public ArrayList userobjects;
	public static LruCache<String, Bitmap> photocache;
	
	public Getimages(ArrayList userobjects) {
       this.userobjects = userobjects;

	}

    public void creatememcache(){
		 final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		 final int cacheSize = maxMemory / 8;
		 
		 photocache = new LruCache<String,Bitmap>(cacheSize){
			 @Override
			 protected int sizeOf(String key, Bitmap bitmap){
				 
				 return (bitmap.getRowBytes() * bitmap.getHeight()) / 1000; 
			 }
		 };
		
	}
	
	//public static Getimages getInstance(){
	//	return Instance;
	//}
			
	public void run(){
		this.creatememcache();
		
		for(int i =0; i < this.userobjects.size(); i++){
			user User =	(user) this.userobjects.get(i);
			id =  User.getId();
			url = User.getImgurl();
			
			Bitmap temp;
			try {
				temp = download(url);
				this.cacheimage(id, temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void cacheimage(String id , Bitmap bitmap){
		Log.d(id, "test");
		if (getimage(id) == null && bitmap != null )  {
			photocache.put(id, bitmap);
		}
	}
	
	public Bitmap getimage(String id){
		return (Bitmap) photocache.get(id);
	}

		public boolean writetosd(){
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				return true;
			}
			return false;
		}
		
		public boolean readssd(){
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
				return true;
			}
			return false;
		}
					
		private Bitmap download(String url) throws IOException {
			HttpClient httpclient = new DefaultHttpClient();
			
			try {
				HttpGet httpget = new HttpGet(url);
				httpget.addHeader("Authorization", token);
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();

				if(entity != null) { 
					InputStream instream = entity.getContent();
					Bitmap image = BitmapFactory.decodeStream(instream);
					
					return image;
				}
				return Bitmap.createBitmap(1,1,Bitmap.Config.ALPHA_8);
			
			}
			
			catch(IOException e){
				e.printStackTrace();
			}
			return null;
			
		}
	
	}
