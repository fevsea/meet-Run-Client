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
import edu.upc.fib.meetnrun.views.UserProfileActivity;
import edu.upc.fib.meetnrun.views.UsersListActivity;
import edu.upc.fib.meetnrun.views.fragments.UsersListFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsViewHolder;

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
public class UsersListTest {

    @Rule
    public final ActivityTestRule<UsersListActivity> activityRule = new ActivityTestRule<UsersListActivity>(UsersListActivity.class) {
        protected void beforeActivityLaunched() {
            AdaptersContainer adaptersContainer = AdaptersContainer.getInstance();
            adaptersContainer.setUserAdapter(MockUserAdapter.getInstance());
            CurrentSession.getInstance().setAdapterContainer(adaptersContainer);
            User user = new User(1,"user","name","lastname","08028","Question",5);

            CurrentSession.getInstance().setToken("AAAA");
            CurrentSession.getInstance().setCurrentUser(user);
        }
    };

    private UsersListFragment getActivityFragment() {
        UsersListFragment userListFragment =
                (UsersListFragment) activityRule.getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.activity_contentFrame);
        if (userListFragment == null) {
            userListFragment = new UsersListFragment();
            activityRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_contentFrame,userListFragment)
                    .commit();
        }
        return userListFragment;
    }

    @Test
    public void testRecyclerViewIntent() {
        Intents.init();
        UsersListFragment userListFragment = getActivityFragment();
        onView(withId(R.id.fragment_friends_container)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_friends_container))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(UserProfileActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testRecyclerViewHolder() {
        UsersListFragment userListFragment = getActivityFragment();
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
                TextView timeViewText = item.itemView.findViewById(R.id.meeting_item_username);
                if (timeViewText == null) {
                    return false;
                }
                return timeViewText.getText().toString().contains(text);
            }
        };
    }
}


