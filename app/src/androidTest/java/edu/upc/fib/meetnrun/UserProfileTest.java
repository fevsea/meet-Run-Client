package edu.upc.fib.meetnrun;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created by eric on 14/11/17.
 */

import edu.upc.fib.meetnrun.models.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
<<<<<<< HEAD
@SmallTest
=======
public class UserProfileTest {
>>>>>>> 2ae17c2f8351a6ce00bcc750c10fc2738d3ecdde

<<<<<<< HEAD
public class UserProfileTest {
/*
    private User u;
=======
    private final User u;
>>>>>>> 981d79ddb6ff684e5ab64bd281f30d7c6e8bb630

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
    }

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
<<<<<<< HEAD
    */
=======

>>>>>>> 2ae17c2f8351a6ce00bcc750c10fc2738d3ecdde
}
