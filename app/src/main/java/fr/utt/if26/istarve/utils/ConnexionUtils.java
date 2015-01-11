package fr.utt.if26.istarve.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Thomas on 09/01/2015.
 */
public class ConnexionUtils {

    private static Context mContext;

    public ConnexionUtils(Context context){
        mContext = context;
    }

    public boolean isOnline(){
        ConnectivityManager cm =(ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
