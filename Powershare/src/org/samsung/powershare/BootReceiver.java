package org.samsung.powershare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import vendor.samsung.hardware.powershare.V1_0.IPowerShare;
import android.os.RemoteException;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerShareBootReceiver";
    private static final String KEY_POWERSHARE_SWITCH = "powershare_switch";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "Boot completed intent received");

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean isPowerShareEnabled = sharedPrefs.getBoolean(KEY_POWERSHARE_SWITCH, false);

            try {
                IPowerShare powerShare = IPowerShare.getService();
                powerShare.setRtxMode(isPowerShareEnabled);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to set PowerShare enabled state", e);
            }
        }
    }
}
