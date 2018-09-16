package swarm.swarmcomposerapp;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import swarm.swarmcomposerapp.ActivitiesAndViews.ListActivity;
import swarm.swarmcomposerapp.ActivitiesAndViews.MainActivity;
import swarm.swarmcomposerapp.Model.SimpleUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EspressoTests {
    private String correct_username = "d.d@de";
    private String incorrect_username = "a.d@de";
    private String correct_password = "123";
    private String incorrect_password = "111";


    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule
    public ActivityTestRule<ListActivity> activityTestRule =
            new ActivityTestRule<>(ListActivity.class);

    // Looks for an EditText with id = "R.id.etInput"
    // Types the text "Hello" into the EditText
    // Verifies the EditText has text "Hello"
    @Test
    public void test(){
        onView(withId(R.id.textView3)).perform(click());
        onView(withText(R.string.settings)).check(matches(isDisplayed()));
        tryLogin(true, true);
        tryLogin(true, false);
        tryLogin(false, true);
        tryLogin(false, false);
    }


    private void tryLogin(boolean username, boolean password) {
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
        onView(withId(R.id.text_username)).check(matches(isDisplayed()));
        onView(withId(R.id.text_password)).check(matches(isDisplayed()));
        //TODO delete anythng in username field
        onView(withId(R.id.text_username)).perform(typeText(username ? correct_username : incorrect_username));
        onView(withId(R.id.text_password)).perform((typeText(password ? correct_password : incorrect_password)));
        onView(withId(R.id.button_login)).perform(click());
        //user should be logged in by now

        if(username && password) {
            onView(withId(R.id.button_logout)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.button_login)).check(matches(isDisplayed()));
        }
        onView(withId(R.id.button_logout)).perform(click());
    }
}
