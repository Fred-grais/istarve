package fr.utt.if26.istarve.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class Gps {

    private static Context mContext;
    private static final String TAG = Gps.class.getSimpleName();

    public Gps(Context context){
        mContext = context;
    }
    // Get the location manager
    public Location getLocation(){
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Log.v(TAG, locationManager.toString());
        return location;
    }

    //GetDistance
    public float getDistance(float latitude, float longitude){
        Location lRestaurant= new Location("r");
        lRestaurant.setLatitude(latitude);
        lRestaurant.setLongitude(longitude);
        getLocation();
//        Log.v(TAG,Float.toString(getLocation().distanceTo(lRestaurant)/1000));
//        return getLocation().distanceTo(lRestaurant)/1000;
        return 20;
    }
}
