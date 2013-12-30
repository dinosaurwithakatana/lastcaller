package com.dwak.lastcall;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by vishnu on 12/29/13.
 */
public class LastCallDirectDialActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent dialIntent = new Intent(Intent.ACTION_CALL);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String dialNumber = extras.getString("tel");
        Log.d("LastCall", dialNumber);

        dialIntent.setData(Uri.parse("tel:" + dialNumber));
        startActivity(dialIntent);
    }
}
