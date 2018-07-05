package tallerii.stories.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.LOCATION_SERVICE;

public class LocationHelper {

    private LocationManager locationManger;
    private LocationListener locationListener;
    private Context context;
    private Activity activity;

    private Location location;
    private double longitude;
    private double latitude;
    private static Geocoder geocoder;

    public static void initializeGeocoder(Context context) {
        geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
    }
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
                setLocation(location);
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

    protected void setLocation(Location location){
        this.location = location;
    }

    public Location getLocation(){
        locationManger = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        if(locationManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManger.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener,null);
            Location location = locationManger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                this.location = location;
            }

        } else{
            locationManger.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener,null);
            Location location = locationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                this.location = location;
            }
        }

        locationManger.removeUpdates(locationListener);
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
        String location = String.format("(%s,%s)", latitude, longitude);
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                Address address = listAddresses.get(0);
                List<String> locationValues = new ArrayList<>();
                appendNotNull(locationValues, address.getLocality());
                appendNotNull(locationValues, address.getCountryName());
                location = TextUtils.join(", ", locationValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    private static void appendNotNull(List<String> locationValues, String locality) {
        if (!TextUtils.isEmpty(locality)) {
           locationValues.add(locality);
        }
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
