package edu.upc.fib.meetnrun.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

import edu.upc.fib.meetnrun.R;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = TrackingActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Polyline route;
    private List<LatLng> routePoints;

    private int steps = 0;
    private float distance = 0;
    private int calories = 0;
    private float speed = 0;
    private double latitude;
    private double longitude;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private final LatLng mDefaultLocation = new LatLng(41.4, 2.1);
    private static final int DEFAULT_ZOOM = 17;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private GoogleApiClient mClient = null;

    Chronometer chronometer;
    TextView stepsCounter;
    TextView speedCounter;
    TextView distanceCounter;
    TextView caloriesCounter;

    private OnDataPointListener locationListener =  new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                if (field.equals(Field.FIELD_LATITUDE)) {
                    latitude = val.asFloat();
                }
                else if (field.equals(Field.FIELD_LONGITUDE)) {
                    longitude = val.asFloat();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateLocationViews(new LatLng(latitude, longitude));
                }
            });
        }
    };

    private OnDataPointListener stepListener = new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                if (field.equals(Field.FIELD_STEPS)) {
                    Log.i(TAG, "Updating Steps counter");
                    steps += val.asInt();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stepsCounter.setText(String.valueOf(steps) + " steps");
                        }
                    });
                }
            }
        }
    };

    private OnDataPointListener speedListener = new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                if (field.equals(Field.FIELD_SPEED)) {
                    Log.i(TAG, "Updating speed counter");
                    speed = val.asFloat();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            speedCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f",speed) + " m/s");
                        }
                    });
                }
            }
        }
    };

    private OnDataPointListener distanceListener = new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                if (field.equals(Field.FIELD_DISTANCE)) {
                    Log.i(TAG, "Updating distance counter");
                    distance += val.asFloat();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            distanceCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f", distance) + " m");
                        }
                    });
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        routePoints = new ArrayList<>();

        buildFitnessClient();
        if (!checkPermissions()) {
            getLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        chronometer = findViewById(R.id.chronometer);
        chronometer.start();
        stepsCounter = findViewById(R.id.steps);
        stepsCounter.setText(String.valueOf(steps) + " steps");
        speedCounter = findViewById(R.id.speed);
        speedCounter.setText("0 m/s");
        distanceCounter = findViewById(R.id.distance);
        distanceCounter.setText("0 m");
    }


    private void buildFitnessClient() {
        if (mClient == null && checkPermissions()) {
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.SENSORS_API)
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    Log.i(TAG, "Connected!!!");
                                    // Now you can make calls to the Fitness APIs.
                                    findFitnessDataSources();
                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    // If your connection to the sensor gets lost at some point,
                                    // you'll be able to determine the reason and react to it here.
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                    } else if (i
                                            == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.i(TAG,
                                                "Connection lost.  Reason: Service Disconnected");
                                    }
                                }
                            }
                    )
                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.i(TAG, "Google Play services connection failed. Cause: " +
                                    result.toString());
                        }
                    })
                    .build();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void findFitnessDataSources() {
        /*
        Find Data Sources and register the listeners.
         */
        Fitness.SensorsApi.findDataSources(mClient, new DataSourcesRequest.Builder()
                // At least one datatype must be specified.
                .setDataTypes(DataType.TYPE_LOCATION_SAMPLE, DataType.TYPE_STEP_COUNT_DELTA, DataType.TYPE_DISTANCE_DELTA, DataType.TYPE_CALORIES_EXPENDED, DataType.TYPE_SPEED)
                // Can specify whether data type is raw or derived.
                .setDataSourceTypes(DataSource.TYPE_RAW, DataSource.TYPE_DERIVED)
                .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {
                        Log.i(TAG, "Result: " + dataSourcesResult.getStatus().toString());
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Log.i(TAG, "Data source found: " + dataSource.toString());
                            Log.i(TAG, "Data Source type: " + dataSource.getDataType().getName());

                            //Let's register a listener to receive Activity data!
                            if (dataSource.getDataType().equals(DataType.TYPE_LOCATION_SAMPLE)) {
                                Log.i(TAG, "Data source for LOCATION_SAMPLE found!  Registering.");
                                registerFitnessDataListener(locationListener, dataSource, DataType.TYPE_LOCATION_SAMPLE);
                            }
                            else if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
                                Log.i(TAG, "Data source for STEP_COUNT_DELTA found! Registering.");
                                registerFitnessDataListener(stepListener, dataSource, DataType.TYPE_STEP_COUNT_DELTA);
                            }
                            else if (dataSource.getDataType().equals(DataType.TYPE_SPEED)) {
                                Log.i(TAG, "Data source for TYPE_SPEED found! Registering.");
                                registerFitnessDataListener(speedListener, dataSource, DataType.TYPE_SPEED);
                            }
                            else if (dataSource.getDataType().equals(DataType.TYPE_DISTANCE_DELTA)) {
                                Log.i(TAG, "Data source for TYPE_DISTANCE_DELTA found! Registering.");
                                registerFitnessDataListener(distanceListener, dataSource, DataType.TYPE_DISTANCE_DELTA);
                            }
                        }
                    }
                });
        // [END find_data_sources]
    }

    private void registerFitnessDataListener(OnDataPointListener mListener, DataSource dataSource, DataType dataType) {
        // [START register_data_listener]

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(100, TimeUnit.MILLISECONDS)
                        .setAccuracyMode(SensorRequest.ACCURACY_MODE_HIGH)
                        .setFastestRate(100, TimeUnit.MILLISECONDS)
                        .build(),
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Listener registered!");
                        } else {
                            Log.i(TAG, "Listener not registered.");
                        }
                    }
                });
        // [END register_data_listener]
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableLocationButtonAndView();
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
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
            if (mLocationPermissionGranted) {
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

 //   private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
      /*  try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location location = (Location) task.getResult();
                            updateLocationViews(new LatLng(location.getLatitude(), location.getLongitude()));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            updateLocationViews(mDefaultLocation);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }*/

    private void updateLocationViews(LatLng position) {
        if (route == null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM));
        }
        routePoints.add(position);
        if (route == null) {
            route = mMap.addPolyline(new PolylineOptions().add(position));
        }
        else {
            route.setPoints(routePoints);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
