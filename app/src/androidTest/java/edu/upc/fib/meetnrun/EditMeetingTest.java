package edu.upc.fib.meetnrun;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.views.EditMeetingActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by guillemcastro on 26/10/2017.
 */


@RunWith(AndroidJUnit4.class)
public class EditMeetingTest {

    @Rule
    public ActivityTestRule<EditMeetingActivity> mMainActivityTestRule = new ActivityTestRule<EditMeetingActivity>(EditMeetingActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, EditMeetingActivity.class);
            result.putExtra("id", "3");
            return result;
        }
    };

    @Test
    public void checkTimePickerShowsAndWorks() {

        onView(withId(R.id.change_time_button)).perform(new ViewAction() {
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

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(7, 10));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.meeting_time)).check(matches(withText("07:10")));

    }

    @Test
    public void checkDatePickerShowsAndWorks() {

        onView(withId(R.id.change_date_button)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 7, 1));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.meeting_date)).check(matches(withText("01/07/2017")));
    }

    @Test
    public void checkOnBackButtonADialogAppears() {

        pressBack();

        onView(withText(R.string.edit_meeting_close_dialog_message)).check(matches(isDisplayed()));


    }

}
