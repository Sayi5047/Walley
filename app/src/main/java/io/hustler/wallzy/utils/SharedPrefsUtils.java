package io.hustler.wallzy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import io.hustler.wallzy.BuildConfig;
import io.hustler.wallzy.constants.Constants;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsUtils {
    private Context context;
    private SharedPreferences mShare;
    private String LOCAL_CACHE = "LHGKJHG8687652&^%*&^&^%&^%$&^%h{}{{*&^*^%E^&%E%^$^%$" + BuildConfig.APPLICATION_ID;
    private String CHECKSOM_KEY = "KJGKJHGKJ&I&^%*&^%*&^%*&^%&^%*&%^{}{*&^%$^%$^%" + BuildConfig.APPLICATION_ID;

    public SharedPrefsUtils(Context context) {
        this.context = context;
        mShare = context.getSharedPreferences(this.LOCAL_CACHE, Context.MODE_PRIVATE);
    }


    public String getString(String Key) {
        return mShare.getString(Key, "");
    }

    public int getInt(String Key) {
        return mShare.getInt(Key, -1);
    }

    public boolean getBoolean(String Key) {
        return mShare.getBoolean(Key, false);
    }

    public double getDouble(String Key) {
        return Double.parseDouble(String.valueOf(mShare.getString(Key, "-1.0")));
    }

    public long getLong(String Key) {
        return mShare.getLong(Key, -1L);
    }


    public boolean putString(String Key, String stringValue) {
        try {
            SharedPreferences.Editor mEditor = mShare.edit();
            mEditor.putString(Key, stringValue);
            mEditor.apply();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void putInt(String Key, int intValue) {
        SharedPreferences.Editor mEditor = mShare.edit();
        mEditor.putInt(Key, intValue);
        mEditor.apply();
    }

    public boolean putBoolean(String Key, boolean booleanValue) {
        try {

            SharedPreferences.Editor mEditor = mShare.edit();
            mEditor.putBoolean(Key, booleanValue);
            mEditor.apply();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean putDouble(String Key, double doubleValue) {
        try {
            SharedPreferences.Editor mEditor = mShare.edit();
            mEditor.putString(Key, String.valueOf(doubleValue));
            mEditor.apply();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean putLong(String Key, long longValue) {
        try {
            SharedPreferences.Editor mEditor = mShare.edit();
            mEditor.putLong(Key, longValue);
            mEditor.apply();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean storeUserData(FirebaseUser firebaseUser) {
        SharedPreferences.Editor mEditor = mShare.edit();
        mEditor.putString(Constants.SP_USERDATA_KEY, new Gson().toJson(firebaseUser));
        return mEditor.commit();

    }

    public FirebaseUser getUserData() {
        return new Gson().fromJson(mShare.getString(Constants.SP_USERDATA_KEY, null), FirebaseUser.class);
    }


    public void clearAllUserData() {
        SharedPreferences preferences = context
                .getSharedPreferences(this.LOCAL_CACHE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
