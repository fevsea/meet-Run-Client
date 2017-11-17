package edu.upc.fib.meetnrun;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
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
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.FriendsActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.MeetingListActivity;
import edu.upc.fib.meetnrun.views.UsersListActivity;
import edu.upc.fib.meetnrun.views.fragments.FriendsFragment;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsViewHolder;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsViewHolder;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by eric on 14/11/17.
 */

@RunWith(AndroidJUnit4.class)
public class FriendsTest {

    @Rule
    public ActivityTestRule<FriendsActivity> activityRule = new ActivityTestRule<FriendsActivity>(FriendsActivity.class) {
        protected void beforeActivityLaunched() {
            AdaptersContainer adaptersContainer = AdaptersContainer.getInstance();
            adaptersContainer.setFriendsAdapter(MockFriendsAdapter.getInstance());
            CurrentSession.getInstance().setAdapterContainer(adaptersContainer);
            User user = new User(1,"user","name","lastname","08028","Question",5);

            CurrentSession.getInstance().setToken("AAAA");
            CurrentSession.getInstance().setCurrentUser(user);
        }
    };

    private FriendsFragment getActivityFragment() {
        FriendsFragment friendsFragment =
                (FriendsFragment) activityRule.getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.activity_contentFrame);
        if (friendsFragment == null) {
            friendsFragment = new FriendsFragment();
            activityRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_contentFrame,friendsFragment)
                    .commit();
        }
        return friendsFragment;
    }

    /*@Test
    public void testFragmentFab() {
        Intents.init();
        FriendsFragment friendsFragment = getActivityFragment();
        onView(withId(R.id.activity_fab)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_fab)).perform(click());
        intended(hasComponent(UsersListActivity.class.getName()));
        Intents.release();
    }*/

    @Test
    public void testRecyclerViewIntent() {
        Intents.init();
        FriendsFragment friendsFragment = getActivityFragment();
        onView(withId(R.id.fragment_friends_container)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_friends_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(FriendProfileActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testRecyclerViewHolder() {
        FriendsFragment friendsFragment = getActivityFragment();
        onView(withId(R.id.fragment_friends_container))
                .perform(RecyclerViewActions.scrollToHolder(
                        withViewHolder("ericR")
                ));
    }

    public static Matcher<RecyclerView.ViewHolder> withViewHolder(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, FriendsViewHolder>(FriendsViewHolder.class) {

            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(FriendsViewHolder item) {
                TextView timeViewText = (TextView) item.itemView.findViewById(R.id.meeting_item_username);
                if (timeViewText == null) {
                    return false;
                }
                return timeViewText.getText().toString().contains(text);
            }
        };
    }
}
