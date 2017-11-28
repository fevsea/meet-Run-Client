package edu.upc.fib.meetnrun;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.views.TrackingActivity;

/**
 * Created by guillemcastro on 01/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TrackingTest {

    @Rule
    public ActivityTestRule<TrackingActivity> mMainActivityTestRule = new ActivityTestRule<>(TrackingActivity.class);

    @Test
    public void test() {

    }

}
