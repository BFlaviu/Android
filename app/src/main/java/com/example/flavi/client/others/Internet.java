package com.example.flavi.client.others;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.flavi.client.AppContext;

public class Internet {

    private Internet(){}

    private static Boolean internetOn = null;

    public static boolean checkForInternet(){
        internetOn  = null;
        if(internetOn  == null){
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            internetOn  = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return internetOn ;

    }
}
