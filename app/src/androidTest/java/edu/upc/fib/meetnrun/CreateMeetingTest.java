package edu.upc.fib.meetnrun;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.LoginActivity;
import edu.upc.fib.meetnrun.views.fragments.CreateMeetingFragment;
import edu.upc.fib.meetnrun.views.fragments.MeetingInfoFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsViewHolder;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static edu.upc.fib.meetnrun.TestUtils.TEST_NEW_DESCRIPTION;
import static edu.upc.fib.meetnrun.TestUtils.TEST_NEW_LEVEL;
import static edu.upc.fib.meetnrun.TestUtils.TEST_NEW_MEETING;
import static edu.upc.fib.meetnrun.TestUtils.TEST_PASSWORD;
import static edu.upc.fib.meetnrun.TestUtils.TEST_USERNAME;
import static edu.upc.fib.meetnrun.TestUtils.getCurrentDay;
import static edu.upc.fib.meetnrun.TestUtils.getCurrentHour;
import static edu.upc.fib.meetnrun.TestUtils.getCurrentMinute;
import static edu.upc.fib.meetnrun.TestUtils.getCurrentMonth;
import static edu.upc.fib.meetnrun.TestUtils.getCurrentYear;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class CreateMeetingTest {


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
        onView(withId(R.id.activity_fab)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_fab)).perform(click());
    }

    @Test
    public void testCreateMeeting() throws InterruptedException {
        onView(withId(R.id.name)).perform(typeText(TEST_NEW_MEETING));
        onView(withId(R.id.pickDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(getCurrentYear(),getCurrentMonth(),getCurrentDay()));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.pickHour)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(getCurrentHour(),getCurrentMinute()));
        onView(withText("OK")).perform(click());
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.level)).perform(typeText(TEST_NEW_LEVEL)).perform(closeSoftKeyboard());
        onView(withId(R.id.description)).perform(typeText(TEST_NEW_DESCRIPTION)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_button)).perform(click());
        Thread.sleep(250);
        onView(withText(R.string.private_no_friends)).perform(click());
    }


}
