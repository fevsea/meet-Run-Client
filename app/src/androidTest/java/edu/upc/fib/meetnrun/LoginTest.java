package edu.upc.fib.meetnrun;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.views.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by eric on 27/10/17.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LoginTest {

    private static final String EXTRA_USERNAME = "ericR";
    private static final String EXTRA_PASSWORD = "ericR";

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void testLoginFields() {

        onView(withId(R.id.editUsername)).check(matches(withText(EXTRA_USERNAME)));
        onView(withId(R.id.editPassword)).check(matches(withText(EXTRA_PASSWORD)));
        onView(withId(R.id.login)).perform(scrollTo(),click());
    }
}
