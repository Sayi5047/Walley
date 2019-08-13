package io.hustler.wallzy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetUtils {
    private static InternetUtils internetUtils;

    private InternetUtils() {

    }

    public static InternetUtils getInstance() {
        if (null != internetUtils) {
            return internetUtils;
        } else {
            return new InternetUtils();
        }
    }

    public boolean isInterNetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
