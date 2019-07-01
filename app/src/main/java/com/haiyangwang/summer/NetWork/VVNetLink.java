package com.haiyangwang.summer.NetWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class VVNetLink extends Object {

    private  VVNetLink() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean getNetIsAvaliable() {


        NetworkInfo info = getConnectivityManager().getActiveNetworkInfo();
        if (info != null) {
            return true;
        }
        return false;
    }

    private static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager)ApplicationContext.INSTANCE.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
