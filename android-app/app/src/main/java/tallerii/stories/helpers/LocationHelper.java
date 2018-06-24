package tallerii.stories.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.LOCATION_SERVICE;

public class LocationHelper {

    public static final Integer LOCATION_REFRESH_TIME = 0;
    public static final Integer LOCATION_REFRESH_DISTANCE = 0;

    private LocationManager locationManger;
    private LocationListener locationListener;
    private Context context;
    private Activity activity;

    private Location location;
    private double longitude;
    private double latitude;

    public LocationHelper(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
        locationListener = getLocationListener();
    }

    // location listener for get location
    private LocationListener getLocationListener() {
        return new LocationListener() {

            @Override
            public void onLocationChanged(final Location location) {
                //location = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    public Location getLocation(){
        locationManger = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);
        location = locationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location;
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public static String getLocationString(double latitude, double longitude) {
        return String.format("(%s,%s)", latitude, longitude);
    }

    public static LatLng getLocation(String location) {
        String regex ="([\\d.+-]+),([\\d.+-]+)";
        Matcher matcher = Pattern.compile(regex).matcher(location);
        if (matcher.find()) {
            return new LatLng(Double.valueOf(matcher.group(1)),Double.valueOf(matcher.group(2)));
        } else {
            return new LatLng(0, 0);
        }
    }
}
