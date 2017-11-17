package edu.upc.fib.meetnrun;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created by eric on 14/11/17.
 */

import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.views.EditMeetingActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.ProfileActivity;
import edu.upc.fib.meetnrun.models.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UserProfileTest {

    private User u;

    @Rule
    public ActivityTestRule<ProfileActivity> activityRule = new ActivityTestRule<ProfileActivity>(
            ProfileActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
          /*  u = new User();
            u.setId(1);
            u.setUsername("Monica");
            u.setFirstName("Monica");
            u.setLastName("Follana");
            u.setPostalCode("08028");
            u.setQuestion("Question");

            CurrentSession.getInstance().setCurrentUser(u);*/
        }
    };

    @Test
    public void testVisibility() {
      //  onView(withId(R.id.fragment_meeting_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testFragmentTestView() {
        onView(withId(R.id.userName)).check(matches(withText(u.getUsername())));
        onView(withId(R.id.completeName)).check(matches(withText(u.getFirstName() + " " + u.getLastName())));
        onView(withId(R.id.userName)).check(matches(withText(u.getUsername())));
    }

}
