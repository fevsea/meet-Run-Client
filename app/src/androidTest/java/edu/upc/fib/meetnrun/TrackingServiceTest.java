package edu.upc.fib.meetnrun;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.services.TrackingService;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by guillemcastro on 17/11/2017.
 */

@MediumTest
@RunWith(AndroidJUnit4.class)
public class TrackingServiceTest {

    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();
    private TrackingService service;

    @Before
    public void setUp() throws TimeoutException {
        Intent serviceIntent = new Intent(InstrumentationRegistry.getContext(), TrackingService.class);
        IBinder binder = serviceTestRule.bindService(serviceIntent);
        service = ((TrackingService.TrackingBinder) binder).getService();
    }

    @Test
    public void testReturnsTrackingDataAndBinds() throws TimeoutException {
        assertThat(service.getTrackingData(), is(any(TrackingData.class)));
    }

}
