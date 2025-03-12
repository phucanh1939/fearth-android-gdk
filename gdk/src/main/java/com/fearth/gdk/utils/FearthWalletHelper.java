package com.fearth.gdk.utils;

import android.util.Log;

import wallet.core.jni.CoinType;
import wallet.core.jni.EthereumMessageSigner;
import wallet.core.jni.HDWallet;
import wallet.core.jni.PrivateKey;

public class FearthWalletHelper {
    private static final String TAG = "WalletHelper";

    static {
        System.loadLibrary("TrustWalletCore");
    }

    // Create wallet with a new mnemonic phrase
    public static HDWallet createWallet() {
        HDWallet wallet = new HDWallet(128, ""); // 128-bit entropy, empty passphrase
        Log.d(TAG, "<createWallet> phrase: " + wallet.mnemonic());
        return wallet;
    }

    // Restore a wallet from a mnemonic phrase
    public static HDWallet getWalletFromPhrase(String phrase) {
        try {
            return new HDWallet(phrase, ""); // Empty passphrase
        } catch (Exception e) {
            Log.e(TAG, "<getWalletFromPhrase> error: " + e.getMessage());
            return null;
        }
    }

    public static String signMessage(HDWallet wallet, String message) {
        PrivateKey privateKey = wallet.getKeyForCoin(CoinType.ETHEREUM);
        return "0x" + EthereumMessageSigner.signMessage(privateKey, message);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
