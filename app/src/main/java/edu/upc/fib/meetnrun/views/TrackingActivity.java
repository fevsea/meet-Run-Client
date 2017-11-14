package edu.upc.fib.meetnrun.views;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.services.TrackingService;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = TrackingActivity.class.getSimpleName();
    public static final String BROADCAST_TRACKING_DATA_ACTION = "edu.upc.fib.meetnrun.trackingdata";
    public static final String BROADCAST_TRACKING_ERROR = "edu.upc.fib.meetnrun.trackingerror";
    public static final int ERROR_WITH_GOOGLE_API = 0;

    //Map, route line and route points
    private GoogleMap mMap;
    private Polyline route;
    private List<LatLng> routePoints;

    private IntentFilter mIntentFilter;

    private TrackingData trackingData;

    private boolean isPaused;

    private static final int DEFAULT_ZOOM = 17;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private GoogleApiClient mClient = null;

    FusedLocationProviderClient mFusedLocationProviderClient;

    Chronometer chronometer;
    TextView stepsCounter;
    TextView speedCounter;
    TextView distanceCounter;
    TextView caloriesCounter;
    FloatingActionButton pauseButton;

    private boolean triedToReconnect = false;

    private Integer meetingId;

    private TrackingService trackingService;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        /*Integer meetingId = getIntent().getIntExtra("id", -1);
        if (meetingId == -1) {
            Toast.makeText(this, R.string.tracking_error_loading, Toast.LENGTH_LONG).show();
            finish();
        }*/

        routePoints = new ArrayList<>();

        if (!checkPermissions()) {
            getLocationPermission();
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        chronometer = findViewById(R.id.chronometer);
        chronometer.start();
        stepsCounter = findViewById(R.id.steps);
        stepsCounter.setText(String.valueOf(0) + " steps");
        speedCounter = findViewById(R.id.speed);
        speedCounter.setText("0 m/s");
        distanceCounter = findViewById(R.id.distance);
        distanceCounter.setText("0 m");
        caloriesCounter = findViewById(R.id.calories);
        caloriesCounter.setText("0 kcal");

        pauseButton = findViewById(R.id.pause_fab);

        pauseButton.setOnClickListener(this);

        isPaused = false;

        trackingData = new TrackingData(0f, 0f, 0, 0f, new ArrayList<LatLng>(), 0);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BROADCAST_TRACKING_DATA_ACTION);
        mIntentFilter.addAction(BROADCAST_TRACKING_ERROR);

        Intent serviceIntent = new Intent(this, TrackingService.class);
        startService(serviceIntent);
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(!checkPermissions()) {
            getLocationPermission();
        }

        enableLocationButtonAndView();
        centerMapToStartPosition();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableLocationButtonAndView();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        enableLocationButtonAndView();
    }

    private void enableLocationButtonAndView() {
        /*
        Enables the 'center to location' button and enables showing the current location with a blue dot.
         */
        if (mMap == null) {
            return;
        }
        try {
            if (checkPermissions()) {
                Log.e(TAG, "Location Permission Granted!");
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                Log.e(TAG, "Location Permission not Granted!");
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void centerMapToStartPosition() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    try {
        if (checkPermissions()) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location location = (Location) task.getResult();
                            if (location != null) {
                                ArrayList<LatLng> routePoints = new ArrayList<LatLng>();
                                routePoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                                updateLocationViews(routePoints);
                            }
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationViews(List<LatLng> routePoints) {
        if (routePoints.size() >= 1) {
            LatLng lastPosition = routePoints.get(routePoints.size()-1);
            if (route == null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, DEFAULT_ZOOM));
                route = mMap.addPolyline(new PolylineOptions().add(lastPosition));
            }
            else {
                route.setPoints(routePoints);
            }
        }
    }

    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, TrackingService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void onDestroy() {
        stopService(new Intent(this, TrackingService.class));
        super.onDestroy();
    }

    public void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBound && trackingService != null) {
            updateUI(trackingService.getTrackingData());
        }
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pause_fab: {
                save();
            }
        }
    }

    private void save() {
        SaveTrackingData saveTrackingData = new SaveTrackingData();
        saveTrackingData.execute(trackingData);
        stopService(new Intent(this, TrackingService.class));
    }

    private class SaveTrackingData extends AsyncTask<TrackingData, String, Boolean> {

        Exception exception = null;
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(TrackingActivity.this);
            mProgressDialog.setTitle(R.string.saving);
            mProgressDialog.setMessage(getResources().getString(R.string.saving_tracking_data));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(TrackingData... params) {
            try {
                Thread.sleep(3000);
                Log.i(TAG, "Saving... " + params[0].toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            if (exception != null || !result) {
                Toast.makeText(TrackingActivity.this, getResources().getString(R.string.tracking_error_toast_message), Toast.LENGTH_LONG).show();
            }
            finish();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e(TAG, "onActivityResult" + resultCode);

        if (requestCode == ConnectionResult.SIGN_IN_REQUIRED) {

            if (resultCode == ConnectionResult.SUCCESS) {

                Log.i(TAG, "SIGN IN SUCCESS, Re-Launching service");
                //startService(new Intent(TrackingActivity.this, TrackingService.class));
                if (trackingService != null) {
                    trackingService.onLogInDone();
                }

            }

        }

    }

    private void updateUI(TrackingData trackingData) {
        distanceCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f", trackingData.getDistance()) + " m");
        speedCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f", trackingData.getAverageSpeed()) + " m/s");
        caloriesCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f", trackingData.getCalories()) + " kcal");
        stepsCounter.setText(String.valueOf(trackingData.getSteps()) + " steps");
        updateLocationViews(trackingData.getRoutePoints());
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            trackingData = intent.getParcelableExtra("Data");
            if (trackingData != null) {
                updateUI(trackingData);
            }
            else {

                PendingIntent pendingIntent = intent.getParcelableExtra("error");
                try {
                    startIntentSenderForResult(pendingIntent.getIntentSender(), ConnectionResult.SIGN_IN_REQUIRED,null,0,0,0);
                }
                catch (IntentSender.SendIntentException ex) {
                    Log.e(TAG, ex.toString());
                }

            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TrackingService.TrackingBinder binder = (TrackingService.TrackingBinder) service;
            trackingService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}