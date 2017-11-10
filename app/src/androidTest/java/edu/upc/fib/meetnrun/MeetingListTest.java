package edu.upc.fib.meetnrun;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.MeetingListActivity;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MeetingListTest{

    @Rule
    public ActivityTestRule<MeetingListActivity> activityRule = new ActivityTestRule<MeetingListActivity>(MeetingListActivity.class) {
        protected void beforeActivityLaunched() {
            CurrentSession.getInstance().setController(new MockDBController());
            User user = new User(1,"user","name","lastname","08028","Question",5);

            CurrentSession.getInstance().setToken("AAAA");
            CurrentSession.getInstance().setCurrentUser(user);
        }
    };
    private MeetingListFragment getActivityFragment() {
        MeetingListFragment meetingListFragment =
                (MeetingListFragment) activityRule.getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.activity_contentFrame);
        if (meetingListFragment == null) {
            meetingListFragment = new MeetingListFragment();
            activityRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_contentFrame,meetingListFragment)
                    .commit();
        }
        return meetingListFragment;
    }


    @Test
    public void testFragmentFab() {
        Intents.init();
        MeetingListFragment meetingListFragment = getActivityFragment();
        onView(withId(R.id.activity_fab)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_fab)).perform(click());
        intended(hasComponent(CreateMeetingActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testRecyclerViewIntent() {
        Intents.init();
        MeetingListFragment meetingListFragment = getActivityFragment();
        onView(withId(R.id.fragment_meeting_container)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_meeting_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(MeetingInfoActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testRecyclerViewHolder() {
        MeetingListFragment meetingListFragment = getActivityFragment();
        onView(withId(R.id.fragment_meeting_container))
                .perform(RecyclerViewActions.scrollToHolder(
                        withViewHolder("Ruta de fibers")
                ));
    }

    public static Matcher<RecyclerView.ViewHolder> withViewHolder(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, MeetingsViewHolder>(MeetingsViewHolder.class) {

            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(MeetingsViewHolder item) {
                TextView timeViewText = (TextView) item.itemView.findViewById(R.id.meeting_item_title);
                if (timeViewText == null) {
                    return false;
                }
                return timeViewText.getText().toString().contains(text);
            }
        };
    }

}