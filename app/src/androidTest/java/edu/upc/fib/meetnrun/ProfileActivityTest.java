package edu.upc.fib.meetnrun;



import android.support.test.espresso.intent.Intents;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import edu.upc.fib.meetnrun.adapters.AdaptersContainer;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.views.EditProfileActivity;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.fragments.ChangePasswordFragment;
import edu.upc.fib.meetnrun.views.fragments.ProfileActivityFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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
            AdaptersContainer adaptersContainer = AdaptersContainer.getInstance();
            adaptersContainer.setUserAdapter(new MockUserAdapter());
            CurrentSession.getInstance().setAdapterContainer(adaptersContainer);
            u = new User(1,"MFM","Monica","Follana","08028", "Question", 5);

            CurrentSession.getInstance().setToken("AAAA");
            CurrentSession.getInstance().setCurrentUser(u);
        }
    };

    private ProfileActivityFragment getActivityFragment() {
        ProfileActivityFragment meetingListFragment =
                (ProfileActivityFragment) activityRule.getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.activity_contentFrame);
        if (meetingListFragment == null) {
            meetingListFragment = new ProfileActivityFragment();
            activityRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_contentFrame,meetingListFragment)
                    .commit();
        }
        return meetingListFragment;
    }

    @Test
    public void testFragmentEditButton() {
        Intents.init();
        ProfileActivityFragment ProfileActivityFragment = getActivityFragment();
        onView(withId(R.id.editProfile_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editProfile_button)).perform(click());
        intended(hasComponent(EditProfileActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testFragmentChangePassButton() {
        Intents.init();
        ProfileActivityFragment ProfileActivityFragment = getActivityFragment();
        onView(withId(R.id.changePass_button)).check(matches(isDisplayed()));
        onView(withId(R.id.changePass_button)).perform(click());
        intended(hasComponent(ChangePasswordFragment.class.getName()));
        Intents.release();
    }

    @Test
    public void testFragmentTestView() {
        onView(withId(R.id.userName)).check(matches(withText(u.getUsername())));
        onView(withId(R.id.completeName)).check(matches(withText(u.getFirstName() + " " + u.getLastName())));
        onView(withId(R.id.userName)).check(matches(withText(u.getUsername())));
        onView(withId(R.id.userName)).check(matches(withText("08028 Barcelona, Spain")));

    }

}
