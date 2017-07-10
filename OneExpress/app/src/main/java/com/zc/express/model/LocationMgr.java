package com.zc.express.model;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.zc.express.utils.Constant;

import java.io.IOException;
import java.util.List;

/**
 * 位置管理类
 * Created by dali on 2017/4/28.
 */

public class LocationMgr {
    private static String TAG = LocationMgr.class.getSimpleName();

    private static LocationListener mLocationListener;

    //监测定位权限
    public static boolean checkLocationPermission(final Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.LOCATION_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    /**
     * 解析位置信息
     *
     * @param activity
     * @param location
     * @return
     */
    private static String getAddressFromLocation(final Activity activity, Location location) {
        Geocoder geocoder = new Geocoder(activity);

        try {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d(TAG, "getAddressFromLocation->lat:" + latitude + ", long:" + longitude);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                //返回当前位置，精度可调
                Address address = addresses.get(0);
                String sAddress;
                if (!TextUtils.isEmpty(address.getLocality())) {
                    if (!TextUtils.isEmpty(address.getFeatureName())) {
                        sAddress = address.getLocality() + " " + address.getFeatureName();
                    } else {
                        sAddress = address.getLocality();
                    }
                } else {
                    sAddress = "定位失败";
                }

                return sAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取位置
     *
     * @param activity
     * @param locationListener
     * @return
     */
    public static boolean getMyLocation(final Activity activity, final onLocationListener locationListener) {
        final LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //notify user
            AlertDialog.Builder builder
                    = new AlertDialog.Builder(activity);
            builder.setMessage("尚未开启位置定位服务");

            builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(intent);
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.show();
            return false;
        }

        if (!checkLocationPermission(activity)) {
            return true;
        }

        Location curLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (null == curLoc) {
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String strAddress = getAddressFromLocation(activity, location);
                    if (TextUtils.isEmpty(strAddress)) {
                        locationListener.onLocationChanged(-1, 0, 0, strAddress);
                    } else {
                        locationListener.onLocationChanged(0, location.getLatitude(), location.getLongitude(), strAddress);
                    }
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onProviderEnabled(String provider) {
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    locationManager.removeUpdates(this);
                }
            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 8000, 0, mLocationListener);
        } else {
            String strAddress = getAddressFromLocation(activity, curLoc);
            if (TextUtils.isEmpty(strAddress)) {
                locationListener.onLocationChanged(-1, 0, 0, strAddress);
            } else {
                locationListener.onLocationChanged(0, curLoc.getLatitude(), curLoc.getLongitude(), strAddress);
            }
        }
        return true;
    }

    public interface onLocationListener {
        void onLocationChanged(int code, double lat1, double long1, String location);
    }
}
