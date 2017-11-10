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

import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.fragments.MeetingInfoFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MeetingInfoTest{
/*
    private static final String EXTRA_TITLE = "Quedada en Barcelona";
    private static final String EXTRA_DESCRIPTION = "La idea es salir por la mañana para hacer unos kms";
    private static final String EXTRA_DATE = "2017-10-10";
    private static final String EXTRA_TIME = "19:04:32";
    private static final String EXTRA_LEVEL = "5";
    private static final String EXTRA_LATITUDE = "50";
    private static final String EXTRA_LONGITUDE = "50";

    @Rule
    public ActivityTestRule<MeetingInfoActivity> activityRule = new ActivityTestRule<MeetingInfoActivity>(
            MeetingInfoActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent meetingInfoIntent = new Intent(targetContext, MeetingInfoActivity.class);
            meetingInfoIntent.putExtra("title",EXTRA_TITLE);
            meetingInfoIntent.putExtra("description",EXTRA_DESCRIPTION);
            meetingInfoIntent.putExtra("date",EXTRA_DATE);
            meetingInfoIntent.putExtra("time",EXTRA_TIME);
            meetingInfoIntent.putExtra("level",EXTRA_LEVEL);
            meetingInfoIntent.putExtra("latitude",EXTRA_LATITUDE);
            meetingInfoIntent.putExtra("longitude",EXTRA_LONGITUDE);
            return meetingInfoIntent;
        }
    };

    private MeetingInfoFragment getActivityFragment() {
        MeetingInfoFragment meetingInfoFragment =
                (MeetingInfoFragment) activityRule.getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.meeting_info_contentFrame);
        if (meetingInfoFragment == null) {
            meetingInfoFragment = new MeetingInfoFragment();
            activityRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.meeting_info_contentFrame,meetingInfoFragment)
                    .commit();
        }
        return meetingInfoFragment;
    }


    @Test
    public void testFragmentTextViews() {
        MeetingInfoFragment meetingInfoFragment = getActivityFragment();
        onView(withId(R.id.meeting_info_title)).check(matches(withText(EXTRA_TITLE)));
        onView(withId(R.id.meeting_info_level)).check(matches(withText(EXTRA_LEVEL)));
        onView(withId(R.id.meeting_info_description)).check(matches(withText(EXTRA_DESCRIPTION)));
        onView(withId(R.id.meeting_info_date)).check(matches(withText(EXTRA_DATE)));
        onView(withId(R.id.meeting_info_time)).check(matches(withText(EXTRA_TIME)));


        //    onView(withId(R.id.changeTextBt)).perform(click());

        // Check that the text was changed.
      //  onView(withId(R.id.textToBeChanged)).check(matches(withText(STRING_TO_BE_TYPED)));
    }

    @Test
    public void testMapFragmentIsVisible() {
        MeetingInfoFragment meetingInfoFragment = getActivityFragment();
        onView(withId(R.id.meeting_info_map)).check(matches(isDisplayed()));
    }
*/
}