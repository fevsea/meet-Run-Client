package edu.upc.fib.meetnrun;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.adapters.AdaptersContainer;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsViewHolder;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MyMeetingsTest{

    @Rule
    public ActivityTestRule<MeetingListActivity> activityRule = new ActivityTestRule<MeetingListActivity>(MeetingListActivity.class) {
        protected void beforeActivityLaunched() {
            AdaptersContainer adaptersContainer = AdaptersContainer.getInstance();
            adaptersContainer.setMeetingAdapter(new MockMeetingAdapter());
            CurrentSession.getInstance().setAdapterContainer(adaptersContainer);
            User user = new User(1,"user","name","lastname","08028","Question",5);

            CurrentSession.getInstance().setToken("AAAA");
            CurrentSession.getInstance().setCurrentUser(user);
        }
    };



    @Test
    public void testFragmentFab() {
        Intents.init();
        onView(withId(R.id.activity_fab)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_fab)).perform(click());
        intended(hasComponent(CreateMeetingActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testRecyclerViewIntent() {
        Intents.init();
        onView(withId(R.id.fragment_meeting_container)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_meeting_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(MeetingInfoActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testRecyclerViewHolder() {
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
                TextView timeViewText = item.itemView.findViewById(R.id.meeting_item_title);
                if (timeViewText == null) {
                    return false;
                }
                return timeViewText.getText().toString().contains(text);
            }
        };
    }

}