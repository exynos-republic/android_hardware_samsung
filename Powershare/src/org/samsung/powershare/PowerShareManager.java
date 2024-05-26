package org.samsung.powershare;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import android.os.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import vendor.samsung.hardware.powershare.V1_0.IPowerShare;

public class PowerShareManager {

    private static final String TAG = "PowerShareManager";

    private IPowerShare mPowerShare;

    private void initHALInterface() {
        try {
            mPowerShare = IPowerShare.getService();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get PowerShare service", e);
        }
    }

    public boolean isRtxModeOn() {
        initHALInterface();
        IPowerShare iPowerShare = this.mPowerShare;
        if (iPowerShare != null) {
            boolean bool1;
            try {
                bool1 = iPowerShare.isRtxEnabled();
            } catch (Exception exception) {
                Log.i(TAG, "failed to read rtx mode: ", exception);
                bool1 = false;
            }
            return bool1;
        }
        return false;
    }

    public void setRtxMode(boolean paramBoolean) {
        initHALInterface();
        IPowerShare iPowerShare = this.mPowerShare;
        if (iPowerShare != null)
            try {
                iPowerShare.setRtxMode(paramBoolean);
            } catch (Exception exception) {
                Log.i(TAG, "failed to set rtx mode: ", exception);
            }
    }

    public int getRtxMinThreshold() {
        initHALInterface();
        IPowerShare iPowerShare = this.mPowerShare;

        int minBattery;

        if (iPowerShare != null) {
            try {
                minBattery = iPowerShare.getMinBattery();
            } catch (Exception exception) {
                Log.i(TAG, "Unable to read min battery level: ", exception);
                // minBattery fallback: disable powershare
                setRtxMode(false);
            }
            return -1;
        } else {
            Log.i(TAG, "Unable to read min battery level: PowerShare is null");
            return -1;
        } 
    }

    public void setRtxMinThreshold(int minBattery) {
        initHALInterface();
        IPowerShare iPowerShare = this.mPowerShare;
        if (iPowerShare != null)
            try {
                iPowerShare.setMinBattery(minBattery);
            } catch (Exception exception) {
                Log.i(TAG, "Unable to set min battery level: ", exception);
            }
    }

}