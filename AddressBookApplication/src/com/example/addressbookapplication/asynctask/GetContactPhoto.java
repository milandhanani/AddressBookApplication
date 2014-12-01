package com.example.addressbookapplication.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * This class will download the contact photo given the URL.
 * TODO: Given more time, I would have fully implemented this calss.
 * @author mdhanani
 *
 */
public class GetContactPhoto extends AsyncTask<String, Void, Bitmap>
{

	@Override
	protected Bitmap doInBackground(String... params) 
	{		
		try 
        {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return null;
        }
	}
	
	@Override
	protected void onPostExecute(Bitmap result) 
	{	
		super.onPostExecute(result);
	}

}
