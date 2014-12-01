package com.example.addressbookapplication;
import java.util.ArrayList;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.addressbookapplication.adapter.ContactListAdapter;
import com.example.addressbookapplication.asynctask.GetContactList;
import com.example.addressbookapplication.objects.Contact;
import com.google.android.gms.common.AccountPicker;

public class MainActivity extends Activity 
{
	
	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	static final int AUTH_REQUEST_CODE = 100;
	
	static final String MUST_SELECT_EMAIL = "You must either add a new google account or select an existing google account in order to get your Google contacts.";
	static final String MUST_ALLOW_PERMISSION = "You must allow Address book application the access to your contacts";

	private ContactListAdapter mContactListAdapter;
	private ArrayList<Contact> mContactList;
	
	private String mEmail; // Received from newChooseAccountIntent(); passed to getToken()
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getEmailOrContactList();
        
        mContactList = new ArrayList<Contact>();
        mContactListAdapter = new ContactListAdapter(getApplicationContext(), R.layout.list_row, mContactList);
        ((ListView) findViewById(R.id.llContactList)).setAdapter(mContactListAdapter);
    }
    
    /**
     * Starts an intent for the user to select one google account.
     * If the user doens't have any google accounts added on their device, 
     * they have an option to add them.
     */
    private void pickUserAccount() 
    {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) 
        {
            if (resultCode == RESULT_OK) 
            {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                // With the account name acquired, go get the auth token
                getEmailOrContactList();
            } 
            else if (resultCode == RESULT_CANCELED) 
            {
                showErrorDialog(MUST_SELECT_EMAIL);
            }
        }
        else if (requestCode == AUTH_REQUEST_CODE)
        {
        	//Address book application doesn't have permission to access the emails contacts
        	if (resultCode == RESULT_OK)
        	{
        		getEmailOrContactList();
        	}
        	else if (resultCode == RESULT_CANCELED)
        	{
        		showErrorDialog(MUST_ALLOW_PERMISSION);
        	}
        }
    }
    
    /**
     * Displays a alertDialog to the user with the given message.
     * @param message - Body text used by the alertDialog
     */
    private void showErrorDialog(String message)
    {
    	//User pressed cancel on the dialog.. display a message
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
    	alertDialogBuilder.setTitle("Error");
    	alertDialogBuilder.setMessage(message);
    	alertDialogBuilder.setPositiveButton("Ok", new OnClickListener() 
    	{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				getEmailOrContactList();
			}
		});
    	
    	AlertDialog alertDialog = alertDialogBuilder.create();
    	alertDialog.show();
    }
    
    /**
     * If the user hasn't selected a valid email address from the 
     * select account dialog, show the dialog again, otherwise,
     * proceede to get contact list from Google.
     */
    private void getEmailOrContactList() 
    {
        if (mEmail == null) 
        {
        	pickUserAccount();
        } 
        else 
        {
            new GetContactList(MainActivity.this).execute(mEmail);
        }
    }
    
    //Getters and Setters

    public ContactListAdapter getContactListAdapter() 
    {
		return mContactListAdapter;
	}

	public void setContactListAdapter(ContactListAdapter mContactListAdapter) 
	{
		this.mContactListAdapter = mContactListAdapter;
	}

	public ArrayList<Contact> getContactList() 
	{
		return mContactList;
	}

	public void setContactList(ArrayList<Contact> mContactList) 
	{
		this.mContactList = mContactList;
	}
    
}