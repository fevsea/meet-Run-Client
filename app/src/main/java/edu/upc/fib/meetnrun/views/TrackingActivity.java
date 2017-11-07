package edu.upc.fib.meetnrun.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.location.Location;
import android.os.AsyncTask;
import android.os.SystemClock;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
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
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.TrackingData;

import static android.icu.text.DateFormat.getTimeInstance;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = TrackingActivity.class.getSimpleName();

    //Map, route line and route points
    private GoogleMap mMap;
    private Polyline route;
    private List<LatLng> routePoints;

    //Current values of the Fitness data
    private int steps = 0;
    private float currentDistance = 0f;
    private float calories = 0f;
    private float speed = 0f;
    private double latitude;
    private double longitude;

    private boolean isPaused;
    private boolean isOnBackground;

    private long startTimeMillis;
    private long totalTimeMillis;
    private long timeSinceBackground;

    //Used to calculate speed
    private float lastDistance = 0f;
    private long timeSinceLastSpeedUpdate;

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
    FloatingActionButton stopButton;

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
                            //speedCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f",speed) + " m/s");
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
                    currentDistance += val.asFloat();
                    long currentTime = SystemClock.elapsedRealtime();
                    float timeDelta = (currentTime - timeSinceLastSpeedUpdate)/1000.0f;
                    if (timeDelta > 0)
                        speed = (currentDistance-lastDistance)/(timeDelta);
                    else
                        speed = 0;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            distanceCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f", currentDistance) + " m");
                            speedCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f", speed) + " m/s");
                        }
                    });
                    lastDistance = currentDistance;
                    timeSinceLastSpeedUpdate = currentTime;
                }
            }
        }
    };

    private OnDataPointListener caloriesListener = new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                calories = val.asFloat();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        caloriesCounter.setText(String.format(Locale.forLanguageTag("ES"), "%.2f", calories) + " kcal");
                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        routePoints = new ArrayList<>();

        buildFitnessClient();
        if (!checkPermissions()) {
            getLocationPermission();
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startTimeMillis = SystemClock.elapsedRealtime();
        timeSinceLastSpeedUpdate = startTimeMillis;
        totalTimeMillis = 0;

        chronometer = findViewById(R.id.chronometer);
        chronometer.start();
        stepsCounter = findViewById(R.id.steps);
        stepsCounter.setText(String.valueOf(steps) + " steps");
        speedCounter = findViewById(R.id.speed);
        speedCounter.setText("0 m/s");
        distanceCounter = findViewById(R.id.distance);
        distanceCounter.setText("0 m");
        caloriesCounter = findViewById(R.id.calories);
        caloriesCounter.setText("0 kcal");

        pauseButton = findViewById(R.id.pause_fab);
        stopButton = findViewById(R.id.stop_fab);

        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        isPaused = false;
        isOnBackground = false;
    }

    private void buildFitnessClient() {
        if ((mClient == null || (!mClient.isConnected() && !mClient.isConnecting())) && checkPermissions()) {
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.SENSORS_API)
                    .addApi(Fitness.RECORDING_API)
                    .addApi(Fitness.HISTORY_API)
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    Log.i(TAG, "Connected!!!");
                                    // Now you can make calls to the Fitness APIs.
                                    if (isOnBackground) {
                                        unsubscribeFitnessDataOnBackground(DataType.TYPE_LOCATION_SAMPLE);
                                        unsubscribeFitnessDataOnBackground(DataType.TYPE_DISTANCE_DELTA);
                                        unsubscribeFitnessDataOnBackground(DataType.TYPE_STEP_COUNT_DELTA);
                                        readBackgroundData();
                                        isOnBackground = false;
                                    }
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
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
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
                            else if (dataSource.getDataType().equals(DataType.TYPE_CALORIES_EXPENDED)) {
                                Log.i(TAG, "Data source for TYPE_CALORIES_EXPENDED found! Registering.");
                                registerFitnessDataListener(caloriesListener, dataSource, DataType.TYPE_CALORIES_EXPENDED);
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
                        Log.i(TAG, "Status info " + status);
                    }
                });
        // [END register_data_listener]
    }

    private void unregisterFitnessDataListener(OnDataPointListener onDataPointListener) {
        Fitness.SensorsApi.remove(mClient, onDataPointListener);
    }

    private void subscribeFitnessDataOnBackground(final DataType dataType) {
        Fitness.RecordingApi.subscribe(mClient, dataType)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, dataType.getName() + " already subscribed on background (SUCCESS)");
                            }
                            else {
                                Log.i(TAG, dataType.getName() + " subscribed on background (SUCCESS)");
                            }
                        }
                        else {
                            Log.i(TAG, dataType.getName() + " could not be subscribed on background (FAIL!)");
                        }
                    }
                });
    }

    private void unsubscribeFitnessDataOnBackground(final DataType dataType) {
        Fitness.RecordingApi.unsubscribe(mClient, dataType)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Successfully unsubscribed for data type: " + dataType.getName());
                        } else {
                            // Subscription not removed
                            Log.i(TAG, "Failed to unsubscribe for data type: " + dataType.getName());
                        }
                    }
                });
    }

    private void readBackgroundData() {
        DataReadRequest stepDataRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(100, TimeUnit.MILLISECONDS)
                .setTimeRange(timeSinceBackground, SystemClock.elapsedRealtime(), TimeUnit.MILLISECONDS)
                .build();
        DataReadRequest distanceDataRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                .bucketByTime(100, TimeUnit.MILLISECONDS)
                .setTimeRange(timeSinceBackground, SystemClock.elapsedRealtime(), TimeUnit.MILLISECONDS)
                .build();
        /*DataReadRequest locationDataRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_LOCATION_SAMPLE, DataType.TYPE_LOCATION_SAMPLE)
                .bucketByTime(100, TimeUnit.MILLISECONDS)
                .setTimeRange(timeSinceBackground, SystemClock.elapsedRealtime(), TimeUnit.MILLISECONDS)
                .build();*/

        for (DataType dt : DataType.getAggregatesForInput(DataType.TYPE_LOCATION_SAMPLE)) {
            Log.i(TAG, "Aggregate for TYPE_LOCATION_SAMPLE: " + dt.getName());
        }

        //DataReadResult stepReadResult = Fitness.HistoryApi.readData(mClient, stepDataRequest).await();
        //DataReadResult distanceReadResult = Fitness.HistoryApi.readData(mClient, distanceDataRequest).await();
        //DataReadResult locationReadResult = Fitness.HistoryApi.readData(mClient, locationDataRequest).await();

        //dumpDataSet(stepReadResult.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA));
        //dumpDataSet(distanceReadResult.getDataSet(DataType.AGGREGATE_DISTANCE_DELTA));
        //dumpDataSet(locationReadResult.getDataSet(DataType.TYPE_LOCATION_SAMPLE));
    }

    private static void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for(Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
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

        if(!checkPermissions()) {
            getLocationPermission();
        }

        enableLocationButtonAndView();
        centerMapToStartPosition();
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

    private void centerMapToStartPosition() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    try {
        if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location location = (Location) task.getResult();
                            updateLocationViews(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

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
        unregisterFitnessDataListener(distanceListener);
        unregisterFitnessDataListener(locationListener);
        unregisterFitnessDataListener(speedListener);
        unregisterFitnessDataListener(stepListener);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        unregisterFitnessDataListener(distanceListener);
        unregisterFitnessDataListener(locationListener);
        unregisterFitnessDataListener(speedListener);
        unregisterFitnessDataListener(stepListener);
        subscribeFitnessDataOnBackground(DataType.TYPE_LOCATION_SAMPLE);
        subscribeFitnessDataOnBackground(DataType.TYPE_DISTANCE_DELTA);
        subscribeFitnessDataOnBackground(DataType.TYPE_STEP_COUNT_DELTA);
        isOnBackground = true;
        timeSinceBackground = SystemClock.elapsedRealtime();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildFitnessClient();
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
                if (isPaused) {
                    stopButton.setVisibility(View.INVISIBLE);
                    pauseButton.setImageResource(R.drawable.ic_pause);
                    resumeTracking();
                }
                else {
                    stopButton.setVisibility(View.VISIBLE);
                    pauseButton.setImageResource(R.drawable.ic_play);
                    pauseTracking();
                }
                isPaused = !isPaused;
                break;
            }
            case R.id.stop_fab: {
                save();
            }
        }
    }

    private void pauseTracking() {
        chronometer.stop();
        unregisterFitnessDataListener(distanceListener);
        unregisterFitnessDataListener(locationListener);
        unregisterFitnessDataListener(speedListener);
        unregisterFitnessDataListener(stepListener);
        long currentTime = SystemClock.elapsedRealtime();
        totalTimeMillis += (currentTime - startTimeMillis);
        timeSinceLastSpeedUpdate = currentTime;
        startTimeMillis = currentTime;
    }

    private void resumeTracking() {
        chronometer.start();
        startTimeMillis = SystemClock.elapsedRealtime();
        findFitnessDataSources();
    }

    private void save() {
        SaveTrackingData saveTrackingData = new SaveTrackingData();
        long time = SystemClock.elapsedRealtime();
        totalTimeMillis += (time - startTimeMillis);
        saveTrackingData.execute(new TrackingData(currentDistance/(totalTimeMillis/1000.0f),currentDistance,steps,routePoints, totalTimeMillis));
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
                Log.i(TAG, params[0].toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            if (exception != null || !result) {
                Toast.makeText(TrackingActivity.this, getResources().getString(R.string.edit_meeting_error_dialog_message), Toast.LENGTH_SHORT).show();
            }
            else {
                TrackingActivity.this.finish();
            }
        }

    }

}