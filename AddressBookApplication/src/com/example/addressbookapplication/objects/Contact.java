package com.example.addressbookapplication.objects;

import android.graphics.Bitmap;

/**
 * The Contact class. Holds all Contact attributes.
 * @author mdhanani
 *
 */
public class Contact 
{
	
	private String mName;
	private String mPhoneNumber;
	private String mPhotoLink;
	private Bitmap mPhoto;
	
	public Contact()
	{
		mName = null;
		mPhoneNumber = null;
		mPhotoLink = null;
	}
	
	public Contact(String name, String phoneNumber, String photoLink) 
	{
		mName = name;
		mPhoneNumber = phoneNumber;
		mPhotoLink = photoLink;
	}
	
	//Getters and Setters

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhoneNumber(String mPhoneNumber) 
	{
		this.mPhoneNumber = mPhoneNumber;
	}

	public String getPhotoLink() {
		return mPhotoLink;
	}

	public void setPhotoLink(String mPhotoLink) 
	{
		this.mPhotoLink = mPhotoLink;
	}

	public Bitmap getPhoto() 
	{
		return mPhoto;
	}

	public void setPhoto(Bitmap mPhoto) 
	{
		this.mPhoto = mPhoto;
	}

}
