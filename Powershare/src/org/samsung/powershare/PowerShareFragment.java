package org.samsung.powershare;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

public class PowerShareFragment extends PreferenceFragmentCompat {
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
                return true;
            });
        }
    }

    int getThresholdLevel() {
        return mSharedPrefs.getInt(KEY_POWERSHARE_THRESHOLD, 20);
    }

    boolean isLowBattery() {
        int batteryLevel = getCurrentBatteryLevel(); // Implement this method to get the current battery level
        int thresholdLevel = getThresholdLevel();
        return batteryLevel < thresholdLevel;
    }

    boolean checkLaunchRequirements() {
        return isLowBattery(); /*
                                 * TODO: Add isOnWirelessCharge() and
                                 * isPowerSaveMode() checks
                                 */
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
            if (enabled) {
                if (checkLaunchRequirements())
                    mPowerShareManager.setRtxMode(enabled);
            } else {
                mPowerShareManager.setRtxMode(enabled);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to set PowerShare enabled state", e);
        }
    }

    private int getCurrentBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getContext().registerReceiver(null, ifilter);
    
        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
    
        if (level == -1 || scale == -1) {
            return 20; // Return a default value if battery level cannot be determined
        }
    
        return (int) ((level / (float) scale) * 100);
    }
    
}
