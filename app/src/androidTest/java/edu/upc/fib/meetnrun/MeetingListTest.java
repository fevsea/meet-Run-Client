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

import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.views.MainActivity;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasPackage;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MeetingListTest{

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class){


    } ;

    private MeetingListFragment getActivityFragment() {
        MeetingListFragment meetingListFragment =
                (MeetingListFragment) activityRule.getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.meeting_list_contentFrame);
        if (meetingListFragment == null) {
            meetingListFragment = new MeetingListFragment();
            activityRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.meeting_list_contentFrame,meetingListFragment)
                    .commit();
        }
        return meetingListFragment;
    }


    @Test
    public void testFragmentTextViews() {
        MeetingListFragment meetingListFragment = getActivityFragment();
        onView(withId(R.id.meeting_list_fab)).check(matches(isDisplayed()));
      //  onView(withId(R.id.meeting_list_fab)).perform(click());
      //  intended(hasPackage("edu.upc.fib.meetnrun.views.CreateMeetingActivity"));

        // Check that the text was changed.
        //  onView(withId(R.id.textToBeChanged)).check(matches(withText(STRING_TO_BE_TYPED)));
    }


}