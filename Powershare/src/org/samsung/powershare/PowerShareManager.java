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
                Log.i(TAG, "isRtxModeOn fail: ", exception);
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
                Log.i(TAG, "setRtxMode fail: ", exception);
            }
    }

}