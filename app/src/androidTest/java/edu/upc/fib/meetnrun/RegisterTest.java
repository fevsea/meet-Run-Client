package edu.upc.fib.meetnrun;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.upc.fib.meetnrun.views.RegisterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by eric on 27/10/17.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RegisterTest {

    private static final String EXTRA_USERNAME = "erick29496";
    private static final String EXTRA_NAME = "Eric";
    private static final String EXTRA_SURNAME = "Rodriguez";
    private static final String EXTRA_POSTALCODE = "08830";
    private static final String EXTRA_PASSWORD1 = "erick";
    private static final String EXTRA_PASSWORD2 = "erick";
    private static final String EXTRA_ANSWER = "Yosha";

    @Rule
    public ActivityTestRule<RegisterActivity> activityRule = new ActivityTestRule<>(RegisterActivity.class) ;

    @Test
    public void testRegisterFields() {

        onView(withId(R.id.editUsernameR)).perform(typeText(EXTRA_USERNAME));
        onView(withId(R.id.editName)).perform(typeText(EXTRA_NAME));
        onView(withId(R.id.editSurname)).perform(typeText(EXTRA_SURNAME));
        onView(withId(R.id.editPostalCode)).perform(typeText(EXTRA_POSTALCODE));
        onView(withId(R.id.editPassword1)).perform(typeText(EXTRA_PASSWORD1));
        onView(withId(R.id.editPassword2)).perform(typeText(EXTRA_PASSWORD2));
        onView(withId(R.id.editAnswer)).perform(typeText(EXTRA_ANSWER));

        
    }

}
