package edu.upc.fib.meetnrun;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import edu.upc.fib.meetnrun.models.TrackingData;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertThat;

/**
 * Created by guillemcastro on 17/11/2017.
 */

public class TrackingDataTest {

    private TrackingData trackingData;

    @Before
    public void setUp() {
        trackingData = new TrackingData(0f, 0f, 0, 0f, new ArrayList<LatLng>(), 0);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        TrackingData clone = (TrackingData) trackingData.clone();
        assertTrue(clone != trackingData);
        assertTrue(clone.equals(trackingData));
    }

}
