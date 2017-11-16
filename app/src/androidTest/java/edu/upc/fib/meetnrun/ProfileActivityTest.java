package edu.upc.fib.meetnrun;


import android.support.test.espresso.intent.Intents;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.views.ProfileActivity;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;
import edu.upc.fib.meetnrun.views.fragments.ProfileActivityFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class ProfileActivityTest {

    private User u;

    @Rule
    public ActivityTestRule<ProfileActivity> activityRule = new ActivityTestRule<ProfileActivity>(
            ProfileActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            u = new User(1,"MFM","Monica","Follana","08028", "Question", 5);

            CurrentSession.getInstance().setToken("AAAA");
            CurrentSession.getInstance().setCurrentUser(u);
        }
    };

    private ProfileActivityFragment getActivityFragment() {
        ProfileActivityFragment profileActivityFragment =
                (ProfileActivityFragment) activityRule.getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.activity_contentFrame);
        if (profileActivityFragment == null) {
            profileActivityFragment = new ProfileActivityFragment();
            activityRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_contentFrame, profileActivityFragment)
                    .commit();
        }
        return profileActivityFragment;
    }

    @Test
    public void testVisibility() {
        Intents.init();
        ProfileActivityFragment profileActivityFragment = getActivityFragment();
        onView(withId(R.id.fragment_profile)).check(matches(isDisplayed()));
    }

    @Test
    public void testFragmentTestView() {
        onView(withId(R.id.userName)).check(matches(withText(u.getUsername())));
        onView(withId(R.id.completeName)).check(matches(withText(u.getFirstName() + " " + u.getLastName())));
        onView(withId(R.id.userName)).check(matches(withText(u.getUsername())));
    }
}
