/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package com.huawei.hms5gkit.presenter;

import android.content.Context;
import android.util.Log;

import com.huawei.hms5gkit.activitys.IHmsKitActivity;
import com.huawei.hms5gkit.utils.ConnectHmsKitUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class HmsKitPresenter {
    private static final String TAG = "[5ghmskit] HmsKitPresenter";
    private static final long SLEEP_TIME = 100L;

    private ConnectHmsKitUtils mHmsKitUtils = ConnectHmsKitUtils.getInstance();
    private WeakReference<IHmsKitActivity> mHmsKitActivity;
    private Context mContext;

    public HmsKitPresenter(Context context, IHmsKitActivity hmsKitActivity) {
        mContext = context;
        mHmsKitActivity = new WeakReference<>(hmsKitActivity);
        ConnectHmsKitUtils.getInstance().setHmsKitActivity(hmsKitActivity);
    }

    public boolean getConnectStatus() {
        return mHmsKitUtils.getConnectStatus();
    }

    // register
    public void registerCallback() {
        Log.i(TAG, "registerCallback");
        boolean flag = mHmsKitUtils.registerCallback(mContext);
        if (flag) {
            Log.i(TAG, "registerCallback success");
        } else {
            Log.i(TAG, "registerCallback failed");
        }
    }

    public void unRegisterCallback() {
        Log.i(TAG, "unRegisterCallback");
        mHmsKitUtils.unRegisterCallback();
        if (mHmsKitActivity != null && mHmsKitActivity.get() != null) {
            mHmsKitActivity.get().showUnRegisterResult();
        }
    }

    public void queryModem(List<String> selected) {
        Log.i(TAG, "queryModem start");
        new Thread(() -> {
            for (String queryItem : selected) {
                if (!mHmsKitUtils.queryModem(queryItem)) {
                    Log.e(TAG, queryItem + " query modem failed.");
                } else {
                    Log.i(TAG, queryItem + " query modem success.");
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (mHmsKitActivity != null && mHmsKitActivity.get() != null) {
                mHmsKitActivity.get().showQueryResult();
            }
        }).start();
    }
}
