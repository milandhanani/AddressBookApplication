package com.example.addressbookapplication.asynctask;

import java.io.IOException;
import java.net.URL;

import com.example.addressbookapplication.MainActivity;
import com.example.addressbookapplication.objects.Contact;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.Link;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.util.ServiceException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * 
 * @author mdhanani
 *
 */
public class GetContactList extends AsyncTask<String, String, Boolean>
{

	String mEmail = null;
	MainActivity mActivity = null;
	
	private ProgressDialog mDialog;
	
	public GetContactList(MainActivity activity)
	{
		mActivity = activity;
	}
	
	@Override
	protected void onPreExecute() 
	{
		mDialog = new ProgressDialog(mActivity);
		mDialog.setMessage("Getting your Google contacts...");
		mDialog.setCancelable(false);
		mDialog.show();
	}
	
	@Override
	protected Boolean doInBackground(String... params) 
	{
		String email = params[0];
		String token = null;
		
		try 
		{
			//Get the auth token
			token = GoogleAuthUtil.getToken(mActivity, email, "oauth2:https://www.googleapis.com/auth/contacts.readonly");
			
			//Incase the auth token is expired, clear it then get the token again
			//TODO: Fix it so that there's a check to see if the token's expired or not
			GoogleAuthUtil.clearToken(mActivity, token);
			
			token = GoogleAuthUtil.getToken(mActivity, email, "oauth2:https://www.googleapis.com/auth/contacts.readonly");
			
			ContactsService serv = new ContactsService("AddressBookApplication");
    		serv.setAuthSubToken(token);
    		getContacts(serv);
		} 
		catch (GooglePlayServicesAvailabilityException e) 
		{
			e.printStackTrace();
			return false;
		} 
		catch (UserRecoverableAuthException e)
		{
			mActivity.startActivityForResult(e.getIntent(), 100);
		}
		catch (GoogleAuthException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		} 
		catch (ServiceException e) 
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) 
	{
		if (result)
		{
			mActivity.getContactListAdapter().notifyDataSetChanged();
		}
		mDialog.dismiss();
	}
	
	/**
	 * Gets all Google contacts and adds them to the arrayList.
	 * This method only gets the Name, Phonenumber and photolink of the contact.
	 * 
	 * @param myService
	 * @throws ServiceException
	 * @throws IOException
	 */
	public void getContacts(ContactsService myService) throws ServiceException, IOException 
	{
		// Request the feed
		URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
		Query query = new Query(feedUrl);
		query.setMaxResults(50);
		query.setStringCustomParameter("sortorder", "ascending");
		query.setStringCustomParameter("showdeleted","false");
		
		ContactFeed resultFeed = myService.query(query, ContactFeed.class);
		for (ContactEntry entry : resultFeed.getEntries()) 
		{
			Contact contact = new Contact();
			
			if (entry.hasName()) 
			{
				Name name = entry.getName();
				if (name.hasFullName()) 
				{
					String fullNameToDisplay = name.getFullName().getValue();
					contact.setName(fullNameToDisplay);
				}
    	    }
    	    
    	    if(entry.getPhoneNumbers() != null && entry.getPhoneNumbers().size() > 0)
    	    {
    	    	contact.setPhoneNumber(entry.getPhoneNumbers().get(0).getPhoneNumber());
    	    }
    	    
    	    Link photoLink = entry.getContactPhotoLink();
    	    String photoLinkHref = photoLink.getHref();
    	    
    	    
    	    if (photoLink.getEtag() != null) 
    	    {
    	    	contact.setPhotoLink(photoLinkHref);
    	    }
    	    
    	    if (contact.getName() != null && contact.getPhoneNumber() != null)
    	    {
    	    	mActivity.getContactList().add(contact);
    	    }
		}
	}
	
}
