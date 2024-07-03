package org.samsung.powershare;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import com.android.settingslib.Utils;
import com.android.settingslib.widget.IllustrationPreference;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.TopIntroPreference;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
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

    // NB. not used
    private int getCurrentBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getContext().registerReceiver(null, ifilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        if (level == -1 || scale == -1) {
            return 50; // Return a default value if battery level cannot be determined
        }

        return (int) ((level / (float) scale) * 100);
    }
    // mPowerSharePreference.setChecked(false);
}
