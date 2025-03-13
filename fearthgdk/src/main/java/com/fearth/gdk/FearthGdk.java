package com.fearth.gdk;

import android.util.Log;

import com.fearth.gdk.defines.ErrorCode;
import com.fearth.gdk.data.GdkConfig;
import com.fearth.gdk.utils.JsonHelper;
import com.fearth.gdk.utils.WalletHelper;

import wallet.core.jni.HDWallet;

public class FearthGdk {
    private static FearthGdk instance;
    private static final String TAG = "FearthGdk";

    public interface InitCallback { void invoke(int errorCode); }

    private FearthGdk() {
        
    }

    // synchronized is used to prevent create multiple instance (thread safety)
    public static synchronized FearthGdk getInstance() {
        if (instance == null) {
            instance = new FearthGdk();
        }
        return instance;
    }

    public void initialize(GdkConfig config, InitCallback callback) {
        Log.d(TAG, "<initialize>" + JsonHelper.toJson(config));
        if (callback != null) callback.invoke(ErrorCode.SUCCESS);
    }

    public String createWallet() {
        HDWallet wallet = WalletHelper.createWallet();
        return wallet.mnemonic();
    }

}
