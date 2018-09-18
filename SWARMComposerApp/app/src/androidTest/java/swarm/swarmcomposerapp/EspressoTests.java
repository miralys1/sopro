package swarm.swarmcomposerapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import swarm.swarmcomposerapp.ActivitiesAndViews.ListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EspressoTests {
    private String correct_username = "d@d.de";
    private String incorrect_username = "a@d.de";
    private String correct_password = "123";
    private String incorrect_password = "111";


    /**
     * Try to log in. Needs to be in SettingsActivity at start. Will always be logged out at the end.
     * @param username true if username should be correct
     * @param password true if password scould be correct
     */
    private void login(boolean username, boolean password) {
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
        onView(withId(R.id.text_username)).check(matches(isDisplayed()));
        onView(withId(R.id.text_password)).check(matches(isDisplayed()));
        onView(withId(R.id.text_username))
                .perform(clearText())
                .perform(typeText(username ? correct_username : incorrect_username));
        onView(withId(R.id.text_password)).perform((typeText(password ? correct_password : incorrect_password)));
        onView(withId(R.id.button_login)).perform(click());
        //user should be logged in by now

        onView(isRoot()).perform(waitFor(3000));

        if(username && password) {
            onView(withId(R.id.button_logout)).check(matches(isDisplayed()));
            onView(withId(R.id.button_logout)).perform(click());
        } else {
            onView(withId(R.id.button_login)).check(matches(isDisplayed()));
        }

    }


    /**
     * Perform action of waiting for a specific time.
     */
    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }


    @Before
    public void init(){
    }


    @Rule
    public ActivityTestRule<ListActivity> activityTestRule =
            new ActivityTestRule<>(ListActivity.class);


    @Test
    public void testLogin(){
        onView(withId(R.id.textView3)).perform(click());
        onView(withText(R.string.settings)).check(matches(isDisplayed()));
        login(true, true);
        login(true, false);
        login(false, true);
        login(false, false);
    }

    @Test
    public void testListView(){
        onView(isRoot()).perform(waitFor(3000));
        //check if public list has items, but the other three don't
        onView(withId(R.id.recyclerView_public)).check(new RecyclerViewHasItemsAssertion(true));
        onView(withId(R.id.recyclerView_viewable)).check(new RecyclerViewHasItemsAssertion(false));
        onView(withId(R.id.recyclerView_owned)).check(new RecyclerViewHasItemsAssertion(false));

        //log in
        onView(withId(R.id.textView3)).perform(click());
        onView(withText(R.string.settings)).check(matches(isDisplayed()));
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
        onView(withId(R.id.text_username)).check(matches(isDisplayed()));
        onView(withId(R.id.text_password)).check(matches(isDisplayed()));
        onView(withId(R.id.text_username))
                .perform(clearText())
                .perform(typeText(correct_username));
        onView(withId(R.id.text_password)).perform((typeText(correct_password)));
        onView(withId(R.id.button_login)).perform(click());
        onView(withId(R.id.button_list)).perform(click());
        //user should be logged in by now

        onView(isRoot()).perform(waitFor(3000));

        //check if any of the non-public lists have items
        onView(withId(R.id.recyclerView_public)).check(new RecyclerViewHasItemsAssertion(true));
        onView(withId(R.id.recyclerView_owned)).check(new RecyclerViewHasItemsAssertion(true));

    }

    /**
     * check whether the given RecyclerView has any number of elements
     */
    public class RecyclerViewHasItemsAssertion implements ViewAssertion {
        private boolean shouldHaveItems;

        public RecyclerViewHasItemsAssertion(boolean shouldHaveItems){
            this.shouldHaveItems = shouldHaveItems;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if(shouldHaveItems)
                assert adapter.getItemCount() > 0;
            else
                assert adapter.getItemCount() == 0;
        }
    }

}


