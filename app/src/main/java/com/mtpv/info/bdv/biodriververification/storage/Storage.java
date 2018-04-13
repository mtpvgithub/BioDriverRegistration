package com.mtpv.info.bdv.biodriververification.storage;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Storage {

    private final SharedPreferences settings;

    public Storage(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveSecure(String key, String value) {
        settings.edit().putString(key, value).apply();
    }

    public void saveSecureBoolean(String key, boolean value) {
        settings.edit().putBoolean(key, value).apply();
    }

    public String getValue(String key) {
        return settings.getString(key, "");
    }

    public boolean getBooleanValue(String key) {
        return settings.getBoolean(key, false);
    }

    public void clearAll() {
        settings.edit().
                remove(Constants.BT_ADDRESS).
                remove(Constants.BT_NAME).
                apply();
    }

}
