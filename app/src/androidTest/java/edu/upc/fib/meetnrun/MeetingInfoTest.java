package edu.upc.fib.meetnrun;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.adapters.AdaptersContainer;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsViewHolder;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MeetingInfoTest{
/*
    private static final int EXTRA_ID = 1;
    private static final String EXTRA_TITLE = "Quedada en Barcelona";
    private static final String EXTRA_DESCRIPTION = "La idea es salir por la mañana para hacer unos kms";
    private static final String EXTRA_OWNER = "owner";
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
            meetingInfoIntent.putExtra("id",EXTRA_ID);
            meetingInfoIntent.putExtra("title",EXTRA_TITLE);
            meetingInfoIntent.putExtra("owner",EXTRA_OWNER);
            meetingInfoIntent.putExtra("description",EXTRA_DESCRIPTION);
            meetingInfoIntent.putExtra("date",EXTRA_DATE);
            meetingInfoIntent.putExtra("time",EXTRA_TIME);
            meetingInfoIntent.putExtra("level",EXTRA_LEVEL);
            meetingInfoIntent.putExtra("latitude",EXTRA_LATITUDE);
            meetingInfoIntent.putExtra("longitude",EXTRA_LONGITUDE);
            return meetingInfoIntent;
        }

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
    public void testFragmentTextViews() {
        onView(withId(R.id.meeting_info_title)).check(matches(withText(EXTRA_TITLE)));
        onView(withId(R.id.meeting_info_level)).check(matches(withText(EXTRA_LEVEL)));
        onView(withId(R.id.meeting_info_description)).check(matches(withText(EXTRA_DESCRIPTION)));
        onView(withId(R.id.meeting_info_date)).check(matches(withText(EXTRA_DATE)));
        onView(withId(R.id.meeting_info_time)).check(matches(withText(EXTRA_TIME)));
        onView(withId(R.id.meeting_info_creator)).check(matches(withText(EXTRA_OWNER)));
    }

    @Test
    public void testMapFragmentIsVisible() {
        onView(withId(R.id.meeting_info_map)).check(matches(isDisplayed()));
    }

    @Ignore
    @Test
    public void testRecyclerViewHolder() {
        onView(withId(R.id.fragment_friends_container))
                .perform(RecyclerViewActions.scrollToHolder(
                        withViewHolder("user1")
                ));
    }

    public static Matcher<RecyclerView.ViewHolder> withViewHolder(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, MeetingsViewHolder>(MeetingsViewHolder.class) {

            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(MeetingsViewHolder item) {
                TextView timeViewText = item.itemView.findViewById(R.id.meeting_item_username);
                if (timeViewText == null) {
                    return false;
                }
                return timeViewText.getText().toString().contains(text);
            }
        };
    }
*/
}