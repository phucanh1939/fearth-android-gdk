package com.fearth.gdk;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fearth.gdk.data.LoginData;
import com.fearth.gdk.utils.FearthHttpHelper;
import com.fearth.gdk.utils.FearthHttpHelper.HttpCallback;
import com.fearth.gdk.utils.FearthJsonHelper;
import com.fearth.gdk.utils.FearthWalletHelper;

import java.util.HashMap;
import java.util.Map;

import wallet.core.jni.CoinType;
import wallet.core.jni.HDWallet;

public class FearthGdk {
    private static FearthGdk instance;
    private static final String TAG = "FearthGdk";

    private FearthGdk() {
        
    }

    // synchronized is used to prevent create multiple instance (thread safety)
    public static synchronized FearthGdk getInstance() {
        if (instance == null) {
            instance = new FearthGdk();
        }
        return instance;
    }

    public void hi() {
        Log.d(TAG, "Hello from Fearth GDK!!!");
    }

    public void testHttp() {
        String testUrl = "https://jsonplaceholder.typicode.com/posts/1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        FearthHttpHelper.get(testUrl, headers, new FearthHttpHelper.HttpCallback() {
            @Override
            public void onResponse(int status, String response) {
                LoginData loginData = FearthJsonHelper.fromJson(response, LoginData.class);
                Log.d(TAG, "HTTP Response: " + FearthJsonHelper.toJson(loginData));
            }
        });
    }

    public void testWallet() {
        HDWallet wallet = FearthWalletHelper.createWallet();
        Log.d(TAG, "CreateWallet phrase: " + FearthJsonHelper.toJson(wallet.mnemonic()));
        Log.d(TAG, "CreateWallet address: " + FearthJsonHelper.toJson(wallet.getAddressForCoin(CoinType.ETHEREUM)));
        String phrase = wallet.mnemonic();
        wallet = FearthWalletHelper.getWalletFromPhrase(phrase);
        if (wallet != null) {
            Log.d(TAG, "RestoreWallet phrase: " + FearthJsonHelper.toJson(wallet.mnemonic()));
            Log.d(TAG, "RestoreWallet address: " + FearthJsonHelper.toJson(wallet.getAddressForCoin(CoinType.ETHEREUM)));
            String signature = FearthWalletHelper.signMessage(wallet, "Hello from FearthGDK!!!");
            Log.d(TAG, "signature: " + signature);
        }
    }

}
