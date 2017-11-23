package edu.upc.fib.meetnrun;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.adapters.AdaptersContainer;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.MeetingListActivity;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsViewHolder;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasPackage;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class CreateMeetingTest {
    private static final String EXTRA_NAME="Meeting test";
    private static final String EXTRA_DESCRIPTION="Meeting description with a large large large length.";

    @Test
    public void checkTimePickerShowsAndWorks() {
        onView(withId(R.id.pickHour)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isEnabled();
            }
            @Override
            public String getDescription() {
                return "Click Change Time button";
            }
            @Override
            public void perform(UiController uiController, View view) {
                view.performClick();
            }
        });
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14, 23));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.meeting_time)).check(matches(withText("14:23")));
    }

    @Test
    public void checkDatePickerShowsAndWorks() {
        onView(withId(R.id.change_date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 12, 15));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.meeting_date)).check(matches(withText("12/15/2017")));
    }

}

