package edu.upc.fib.meetnrun.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.views.TrackingActivity;

/**
 * Created by guillemcastro on 07/11/2017.
 */

public class TrackingService extends Service {

    private final IBinder binder = new TrackingBinder();

    private static final String TAG = "TrackingService";
    private GoogleApiClient mClient;

    private long startTimeMillis;

    private TrackingData trackingData;

    public TrackingService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        startTimeMillis = SystemClock.elapsedRealtime();
        this.trackingData = new TrackingData(0f, 0f, 0, 0f, new ArrayList<LatLng>(), 0);
        Log.d(TAG, "TrackingService created!");
    }

    @Override
    public void onDestroy() {
        if (mClient != null && mClient.isConnected()) {
            mClient.disconnect();
            Log.d(TAG, "GoogleApiClient disconnected");
        }
        Log.d(TAG, "TrackingService destroyed");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                buildFitnessClient();
            }
        }).start();
        return START_REDELIVER_INTENT;
    }

    public void onLogInDone() {
        if (mClient != null)
            mClient.disconnect();
        mClient = null;
        buildFitnessClient();
    }

    private void buildFitnessClient() {
        Log.d(TAG, "buildFitnessClient");
        if (mClient == null || !mClient.isConnected()) {
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.SENSORS_API)
                    .addApi(Fitness.RECORDING_API)
                    .addApi(Fitness.HISTORY_API)
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .addScope(new Scope(Scopes.PROFILE))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    Log.i(TAG, "Connected!!!");
                                    findFitnessDataSourcesAndRegister();
                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                    } else if (i
                                            == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.i(TAG,
                                                "Connection lost.  Reason: Service Disconnected");
                                    }
                                }
                            }
                    ).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.e(TAG, "Google APIs unable to connect");
                            Log.e(TAG, "Reason: " + connectionResult);
                            notifyUIError(connectionResult);
                        }
                    })
                    .build();
            mClient.connect();
        }
        else {
            findFitnessDataSourcesAndRegister();
        }
    }

    private void findFitnessDataSourcesAndRegister() {
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
    }

    private void registerFitnessDataListener(OnDataPointListener mListener, DataSource dataSource, DataType dataType) {

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
    }

    private final OnDataPointListener locationListener =  new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            float latitude = 0f, longitude = 0f;
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
            LatLng currentPosition = new LatLng(latitude, longitude);
            List<LatLng> routePoints = trackingData.getRoutePoints();
            routePoints.add(currentPosition);
            trackingData.setRoutePoints(routePoints);
            trackingData.setTotalTimeMillis(SystemClock.elapsedRealtime() - startTimeMillis);
            notifyUIData();
        }
    };

    private final OnDataPointListener stepListener = new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            int currentStep = 0;
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                if (field.equals(Field.FIELD_STEPS)) {
                    currentStep = val.asInt();
                }
            }
            int steps = trackingData.getSteps() + currentStep;
            trackingData.setSteps(steps);
            trackingData.setTotalTimeMillis(SystemClock.elapsedRealtime() - startTimeMillis);
            notifyUIData();
        }
    };

    private final OnDataPointListener distanceListener = new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            float distance = trackingData.getDistance();
            float speed = 0f;
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                if (field.equals(Field.FIELD_DISTANCE)) {
                    distance += val.asFloat();
                    long currentTime = SystemClock.elapsedRealtime();
                    float timeDelta = (currentTime - startTimeMillis)/1000.0f;
                    if (timeDelta > 0)
                        speed = (distance)/(timeDelta);
                    else
                        speed = 0;
                }
            }
            trackingData.setDistance(distance);
            trackingData.setAverageSpeed(speed);
            trackingData.setTotalTimeMillis(SystemClock.elapsedRealtime() - startTimeMillis);
            notifyUIData();
        }
    };

    private final OnDataPointListener caloriesListener = new OnDataPointListener() {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            float calories = 0f;
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                calories = val.asFloat();
            }
            trackingData.setCalories(calories);
            trackingData.setTotalTimeMillis(SystemClock.elapsedRealtime() - startTimeMillis);
            notifyUIData();
        }
    };

    private void notifyUIData() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(TrackingActivity.BROADCAST_TRACKING_DATA_ACTION);
        broadcastIntent.putExtra("Data", trackingData);
        sendBroadcast(broadcastIntent);
    }

    private void notifyUIError(ConnectionResult connectionResult/*PendingIntent pendingIntent*/) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(TrackingActivity.BROADCAST_TRACKING_ERROR);
        broadcastIntent.putExtra("error", connectionResult);
        sendBroadcast(broadcastIntent);
    }

    public TrackingData getTrackingData() {
        try {
            return (TrackingData) this.trackingData.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public class TrackingBinder extends Binder {

        public TrackingService getService() {
            return TrackingService.this;
        }

    }
}
