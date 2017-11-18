package edu.upc.fib.meetnrun;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.AdaptersContainer;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.EditProfileActivity;
import edu.upc.fib.meetnrun.views.ProfileActivity;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.fragments.EditProfileFragment;
import edu.upc.fib.meetnrun.views.fragments.MeetingListFragment;
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
public class ProfileEditTest {

    private User u;

    @Rule
    public ActivityTestRule<EditProfileActivity> activityRule = new ActivityTestRule<EditProfileActivity>(
            EditProfileActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            AdaptersContainer adaptersContainer = AdaptersContainer.getInstance();
            adaptersContainer.setUserAdapter(new MockUserAdapter());
            CurrentSession.getInstance().setAdapterContainer(adaptersContainer);
            User user = new User(1,"user","name","lastname","08028","Question",5);

            CurrentSession.getInstance().setToken("AAAA");
            CurrentSession.getInstance().setCurrentUser(user);
        }
    };

    private EditProfileFragment getActivityFragment() {
        EditProfileFragment meetingListFragment =
                (EditProfileFragment) activityRule.getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.activity_contentFrame);
        if (meetingListFragment == null) {
            meetingListFragment = new EditProfileFragment();
            activityRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_contentFrame,meetingListFragment)
                    .commit();
        }
        return meetingListFragment;
    }


    @Test
    public void testFragmentTestView() {
        Intents.init();
        EditProfileFragment EditProfileActivityFragment = getActivityFragment();
        onView(withId(R.id.editProfile_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editProfile_button)).perform(click());
        intended(hasComponent(ProfileActivity.class.getName()));
        Intents.release();

    }
}