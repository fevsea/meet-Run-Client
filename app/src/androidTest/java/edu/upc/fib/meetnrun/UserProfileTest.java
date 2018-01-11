package edu.upc.fib.meetnrun;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.DrawerActivity;
import edu.upc.fib.meetnrun.views.LoginActivity;
import edu.upc.fib.meetnrun.views.fragments.CreateMeetingFragment;
import edu.upc.fib.meetnrun.views.fragments.MeetingInfoFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsViewHolder;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static edu.upc.fib.meetnrun.TestUtils.TEST_COMPLETE_NAME;
import static edu.upc.fib.meetnrun.TestUtils.TEST_MEETING;
import static edu.upc.fib.meetnrun.TestUtils.TEST_PASSWORD;
import static edu.upc.fib.meetnrun.TestUtils.TEST_POSTCODE;
import static edu.upc.fib.meetnrun.TestUtils.TEST_USERNAME;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class UserProfileTest {


    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<LoginActivity>(LoginActivity.class) {
        protected void beforeActivityLaunched() {
            TestUtils.deleteToken(InstrumentationRegistry.getTargetContext());
            super.beforeActivityLaunched();
        }
    };


    @Before
    public void setupTest() throws InterruptedException {
        onView(withId(R.id.editUsername)).perform(typeText(TEST_USERNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText(TEST_PASSWORD)).perform(closeSoftKeyboard());
        onView(withId(R.id.layout_buttons_login)).perform(ViewActions.scrollTo());
        Thread.sleep(250);
        onView(withId(R.id.login)).perform(click());
    }

    @Test
    public void testProfileInfo() {
        DrawerActions.openDrawer(R.id.activity_drawerlayout);
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.userName)).check(matches(withText(TEST_USERNAME)));
        onView(withId(R.id.completeName)).check(matches(withText(TEST_COMPLETE_NAME)));
        onView(withId(R.id.userPostCode)).check(matches(withText(TEST_POSTCODE)));
    }

}