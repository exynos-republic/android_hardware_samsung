package org.samsung.powershare;

import android.os.Bundle;

import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;

public class PowerShareActivity extends CollapsingToolbarBaseActivity {

    private static final String TAG_POWERSHARE = "powershare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(
                com.android.settingslib.collapsingtoolbar.R.id.content_frame,
                new PowerShareFragment(), TAG_POWERSHARE).commit();
    }
}
