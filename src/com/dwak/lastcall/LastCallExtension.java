package com.dwak.lastcall;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Contacts;
import android.util.Log;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class LastCallExtension extends DashClockExtension{

	private static final String TAG = LastCallExtension.class.getSimpleName();
	private String lastCallNumber;
	private ContentResolver resolver;
	private String lastCallName;
	private Cursor cur2;
	@Override
	protected void onUpdateData(int arg0) {
		// TODO Auto-generated method stub
		String[] projection = new String[]{CallLog.Calls.NUMBER};
		Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE +" desc");
		try{
			cur.moveToFirst();
			lastCallNumber = cur.getString(0);
			Log.v(TAG,lastCallNumber);
		}
		finally{
			cur.close();
		}

		Intent dialIntent = new Intent(Intent.ACTION_DIAL);
		dialIntent.setData(Uri.parse("tel:"+lastCallNumber));

		publishUpdate(new ExtensionData()
		.visible(true)
		.icon(R.drawable.ic_launcher)
		.status(lastCallNumber)
		.expandedTitle(lastCallNumber)
		.expandedBody("Click to dial!")
		.clickIntent(dialIntent));
	}

}