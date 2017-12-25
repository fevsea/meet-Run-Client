package edu.upc.fib.meetnrun.views;

import android.Manifest;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.SaveTrackingData;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.services.TrackingService;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = TrackingActivity.class.getSimpleName();
    public static final String BROADCAST_TRACKING_DATA_ACTION = "edu.upc.fib.meetnrun.trackingdata";
    public static final String BROADCAST_TRACKING_ERROR = "edu.upc.fib.meetnrun.trackingerror";

    //Map, route line and route points
    private GoogleMap mMap;
    private Polyline route;

    private IntentFilter mIntentFilter;

    private TrackingData trackingData;

    private static final int DEFAULT_ZOOM = 17;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Chronometer chronometer;
    private TextView stepsCounter;
    private TextView speedCounter;
    private TextView distanceCounter;
    private TextView caloriesCounter;
    private FloatingActionButton pauseButton;
    private LinearLayout contentMain;

    private ProgressBar progressBar;

    private Integer meetingId;

    private TrackingService trackingService;
    private boolean mBound;
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 0533;

    private String stepsText;
    private String speedText;
    private String caloriesText;
    private String distanceText;

    private boolean reentrantLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        reentrantLocation = false;

        meetingId = getIntent().getIntExtra("id", -1);
        if (meetingId == -1) {
            Toast.makeText(this, R.string.tracking_error_loading, Toast.LENGTH_LONG).show();
            finish();
        }

        if (!checkPermissions()) {
            getLocationPermission();
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        stepsText = getResources().getString(R.string.tracking_steps);
        distanceText = getResources().getString(R.string.tracking_distance);
        speedText = getResources().getString(R.string.tracking_speed);
        caloriesText = getResources().getString(R.string.tracking_calories);

        chronometer = findViewById(R.id.chronometer);
        chronometer.start();
        stepsCounter = findViewById(R.id.steps);
        stepsCounter.setText(String.format(Locale.forLanguageTag("es"), stepsText, 0));
        speedCounter = findViewById(R.id.speed);
        speedCounter.setText(String.format(Locale.forLanguageTag("es"), speedText, 0.0f));
        distanceCounter = findViewById(R.id.distance);
        distanceCounter.setText(String.format(Locale.forLanguageTag("es"), distanceText, 0.0f));
        caloriesCounter = findViewById(R.id.calories);
        caloriesCounter.setText(String.format(Locale.forLanguageTag("es"), caloriesText, 0.0f));

        pauseButton = findViewById(R.id.pause_fab);

        contentMain = findViewById(R.id.content_main);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        pauseButton.setOnClickListener(this);

        trackingData = new TrackingData(0f, 0f, 0, 0f, new ArrayList<LatLng>(), 0);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BROADCAST_TRACKING_DATA_ACTION);
        mIntentFilter.addAction(BROADCAST_TRACKING_ERROR);

        Intent serviceIntent = new Intent(this, TrackingService.class);
        startService(serviceIntent);

        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_LOCATION_SAMPLE, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        }
        else if(trackingService != null) {
            trackingService.onLogInDone();
        }
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
        enableLocationButtonAndView();
    }

    private void enableLocationButtonAndView() {
        /*
        Enables the 'center to location' button and enables showing the current location with a blue dot.
         */
        if (mMap == null || reentrantLocation) {
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
                reentrantLocation = true;
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
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location location = (Location) task.getResult();
                            if (location != null) {
                                ArrayList<LatLng> routePoints = new ArrayList<>();
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
        callSaveTrackingData(meetingId,trackingData);
    }

    private void callSaveTrackingData(int meetingId, TrackingData trackingData) {
        contentMain.setVisibility(View.GONE);
        pauseButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        SaveTrackingData saveTrackingData = new SaveTrackingData(meetingId) {
            @Override
            public void onResponseReceived() {
                stopService(new Intent(TrackingActivity.this, TrackingService.class));
                finish();
            }
        };
        try {
            saveTrackingData.execute(trackingData);
        }
        catch (AuthorizationException e) {
            Toast.makeText(this, R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
        catch (ForbiddenException e) {
            Toast.makeText(TrackingActivity.this, getResources().getString(R.string.tracking_error_toast_message), Toast.LENGTH_LONG).show();

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e(TAG, "onActivityResult (requestCode+resultCode) " + requestCode+ " " + resultCode);

        if (requestCode == ConnectionResult.SIGN_IN_REQUIRED || requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {

            if (resultCode == ConnectionResult.SUCCESS) {
                Log.i(TAG, "SIGN IN SUCCESS");
            }
            trackingService.onLogInDone();

        }

    }

    private void updateUI(TrackingData trackingData) {
        distanceCounter.setText(String.format(Locale.forLanguageTag("ES"), distanceText, trackingData.getDistance()));
        speedCounter.setText(String.format(Locale.forLanguageTag("ES"), speedText, trackingData.getAverageSpeed()));
        caloriesCounter.setText(String.format(Locale.forLanguageTag("ES"), caloriesText, trackingData.getCalories()));
        stepsCounter.setText(String.format(Locale.forLanguageTag("es"), stepsText ,trackingData.getSteps()));
        updateLocationViews(trackingData.getRoutePoints());
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            trackingData = intent.getParcelableExtra("Data");
            if (trackingData != null) {
                updateUI(trackingData);
            }
            else {

                ConnectionResult connectionResult = intent.getParcelableExtra("error");
                try {
                    connectionResult.startResolutionForResult(TrackingActivity.this, ConnectionResult.SIGN_IN_REQUIRED);
                }
                catch (IntentSender.SendIntentException ex) {
                    Log.e(TAG, ex.toString());
                }

            }
        }
    };

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
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
