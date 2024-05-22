package org.samsung.powershare;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import android.os.RemoteException;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import vendor.samsung.hardware.powershare.V1_0.IPowerShare;

public class PowerShareFragment extends PreferenceFragment {
    private static final String TAG = "PowerShareFragment";
    private static final String KEY_POWERSHARE_SWITCH = "powershare_switch";

    private SharedPreferences mSharedPrefs;
    private SwitchPreference mPowerSharePreference;

    private IPowerShare mPowerShare;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.powershare_preferences, rootKey);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mPowerSharePreference = (SwitchPreference) findPreference(KEY_POWERSHARE_SWITCH);
        //SwitchPreferenceCompat powerShareSwitch = findPreference(KEY_POWERSHARE_SWITCH);

        try {
            mPowerShare = IPowerShare.getService();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get PowerShare service", e);
        }

        if (mPowerShare != null) {
            mPowerSharePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean enabled = (Boolean) newValue;
                setPowerShareEnabled(enabled);
                return true;
            });

            // Initialize switch state based on current PowerShare status
            boolean isEnabled = isPowerShareEnabled();
            mPowerSharePreference.setChecked(isEnabled);
        }
    }

    private boolean isPowerShareEnabled() {
        try {
            return mPowerShare.isEnabled();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to check if PowerShare is enabled", e);
            return false;
        }
    }

    private void setPowerShareEnabled(boolean enabled) {
        try {
            mPowerShare.setEnabled(enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to set PowerShare enabled state", e);
        }
    }
}
