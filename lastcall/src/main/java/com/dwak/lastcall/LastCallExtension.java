package com.dwak.lastcall;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class LastCallExtension extends DashClockExtension {

    private static final String TAG = LastCallExtension.class.getSimpleName();
    private String mLastCallNumber;
    private ContentResolver resolver;
    private String mLastCallName;
    private Cursor cur2;
    public static final String PREF_DIAL = "pref_dial";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onUpdateData(int arg0) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String[] projection = new String[]{CallLog.Calls.NUMBER};
        Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " desc");

        try {
            if (cur.getCount() != 0) {
                cur.moveToFirst();
                mLastCallNumber = cur.getString(0);
                Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode("tel:" + mLastCallNumber));
                cur2 = getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);

                if (cur2 != null) {
                    if (cur2.moveToFirst()) {
                        mLastCallName = cur2.getString(0);
                    } else {
                        mLastCallName = "Unknown";
                    }
                } else {
                    mLastCallName = "Unknown";
                }
                Log.v(TAG, mLastCallNumber);
                Log.v(TAG, mLastCallName);
            } else {
                Log.v(TAG, "No Calls in history");
                mLastCallName = "No Call History";
                mLastCallNumber = "";
            }

        } finally {
            cur.close();
            if (cur2 != null)
                cur2.close();
        }

        final boolean isDirectDial = mSharedPreferences.getBoolean(getString(R.string.pref_dial), false);
        Intent dialIntent = new Intent(isDirectDial ? Intent.ACTION_CALL: Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + mLastCallNumber));

        if (!mLastCallNumber.equals("")) {
            final String expandedBody = "Click to "+ (isDirectDial ? "Call" : "Dial") + "!";
            publishUpdate(new ExtensionData()
                    .visible(true)
                    .icon(R.drawable.ic_launcher)
                    .status(mLastCallName)
                    .expandedTitle(mLastCallName + ": (" + mLastCallNumber + ")")
                    .expandedBody(expandedBody)
                    .clickIntent(dialIntent));
        }
        else {
            publishUpdate(new ExtensionData()
                    .visible(true)
                    .icon(R.drawable.ic_launcher)
                    .status(mLastCallName)
                    .expandedTitle(mLastCallName));
        }
    }

}
