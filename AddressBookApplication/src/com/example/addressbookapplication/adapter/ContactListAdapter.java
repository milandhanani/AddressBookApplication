package com.example.addressbookapplication.adapter;

import java.util.ArrayList;

import com.example.addressbookapplication.R;
import com.example.addressbookapplication.objects.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The Adapter class for Contact List - ListView.
 * @author mdhanani
 *
 */
public class ContactListAdapter extends ArrayAdapter<Contact>
{
	private LayoutInflater mInflater;
	private ArrayList<Contact> mContactList;

	public ContactListAdapter(Context context, int resource, ArrayList<Contact> contactList) 
	{
		super(context, resource, contactList);
		mContactList = contactList;
		mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = convertView;
		
		if (view == null)
		{
			view = mInflater.inflate(R.layout.list_row, parent, false);
		}
		
		Contact currentContact = mContactList.get(position);
		
		((TextView) view.findViewById(R.id.tvName)).setText(currentContact.getName());
		((TextView) view.findViewById(R.id.tvPhoneNumber)).setText(currentContact.getPhoneNumber());		
		//TODO: Implement the photo functionality given more time
		
		return view;
	}

}
