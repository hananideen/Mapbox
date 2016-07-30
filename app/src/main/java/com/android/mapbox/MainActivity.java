package com.android.mapbox;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;
    private LocationServices locationServices;
    private List<User> userList;

    private static final int PERMISSIONS_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this,getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        userList = new ArrayList<>();
        initializeData();
        locationServices = LocationServices.getLocationServices(MainActivity.this);

        mapView = (MapView) findViewById(R.id.mapview);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                //show all marker from initialized User object
                for (User user : userList){
                    mapboxMap.addMarker(new MarkerViewOptions()
                            .position(new LatLng(user.getLat(),user.getLng()))
                            .title(user.getPlate())
                            .anchor(1, (float) 0.0)
                            .icon(setMarkerData(user.getPlate(),user.getProfile())));
                }

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(2.9242157,101.6387169))
                        .zoom(16)
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.location_toggle_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    toggleGps();
                }
            }
        });

    }

    /**
     * to set the marker data into the custom marker layout, then create a bitmap out of it
     * @param plate plate number to be displayed on marker from the User object
     * @param picture User picture from object
     * @return the marker as icon
     */
    private Icon setMarkerData(String plate, String picture){
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        TextView number = (TextView) marker.findViewById(R.id.tv_plate);
        number.setText(plate);
        ImageView profile = (ImageView ) marker.findViewById(R.id.iv_profile);
        profile.setImageBitmap(Utils.setBitmap(picture, this));

        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Icon icon = iconFactory.fromBitmap(Utils.createBitmapFromView(MainActivity.this, marker));
        return icon;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @UiThread
    public void toggleGps() {
        if (!locationServices.areLocationPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        }
        enableLocation(true);
    }

    private void enableLocation(boolean enabled) {
        if (enabled) {
            locationServices.addLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(location))
                                .zoom(15)
                                .build();

                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(position), 7000);
                    }
                }
            });
        }
        map.setMyLocationEnabled(enabled);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation(true);
                }
            }
        }
    }

    /**
     * to manipulate data inside User object
     */
    private void initializeData(){
        userList.add(new User("WJP 3864","android",2.9242157,101.6387169));
        userList.add(new User("ADD 616","google_car",2.9238053,101.6374947));
        userList.add(new User("WD 25","ferrari",2.9217563,101.6365088));
    }
}
