package org.samsung.powershare;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

public class PowerShareFragment extends PreferenceFragment {
    private static final String TAG = "PowerShareFragment";
    private static final String KEY_POWERSHARE_SWITCH = "powershare_switch";
    private static final String KEY_POWERSHARE_THRESHOLD = "powershare_threshold";

    private SharedPreferences mSharedPrefs;
    private SwitchPreference mPowerSharePreference;
    private SeekBarPreference mPowerShareThresholdPreference;

    private PowerShareManager mPowerShareManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.powershare_preferences, rootKey);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mPowerSharePreference = findPreference(KEY_POWERSHARE_SWITCH);
        mPowerShareThresholdPreference = findPreference(KEY_POWERSHARE_THRESHOLD);

        mPowerShareManager = new PowerShareManager();

        mSharedPrefs.edit().putInt(KEY_POWERSHARE_THRESHOLD, getPowerShareMinBatteryLevel()).apply();

        if (mPowerSharePreference != null) {
            mPowerSharePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean enabled = (Boolean) newValue;
                setPowerShareMode(enabled);
                return true;
            });

            // Initialize switch state based on current PowerShare status
            boolean isEnabled = isPowerShareEnabled();
            mPowerSharePreference.setChecked(isEnabled);
        }

        if (mPowerShareThresholdPreference != null) {
            mPowerShareThresholdPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                int threshold = (Integer) newValue;
                mSharedPrefs.edit().putInt(KEY_POWERSHARE_THRESHOLD, threshold).apply();
                setPowerShareMinBatteryLevel(threshold);
                return true;
            });
        }
    }

    private boolean isPowerShareEnabled() {
        try {
            return mPowerShareManager.isRtxModeOn();
        } catch (Exception e) {
            Log.e(TAG, "Failed to check if PowerShare is enabled", e);
            return false;
        }
    }

    private void setPowerShareMode(boolean enabled) {
        try {
            mPowerShareManager.setRtxMode(enabled);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set PowerShare enabled state", e);
        }
    }

    private int getPowerShareMinBatteryLevel() {
        try {
            return mPowerShareManager.getRtxMinThreshold();
        } catch (Exception e) {
            Log.e(TAG, "Failed to set PowerShare enabled state", e);
            return -1;
        }
    }

    private void setPowerShareMinBatteryLevel(int minBattery) {
        try {
            mPowerShareManager.setRtxMinThreshold(minBattery);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set PowerShare enabled state", e);
        }
    }

}
