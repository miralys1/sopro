package swarm.swarmcomposerapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swarm.swarmcomposerapp.ActivitiesAndViews.ListActivity;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.Utils.RetrofitClients;
import swarm.swarmcomposerapp.Utils.ServerCommunication;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;

/**
 * Instrumented test, which will execute on an Android device.
 * Tests if a reload button press results into a request that correctly parses
 * a Json file and stores the result into the LocalCache
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FullRequestTest {

    MockWebServer server;

    MockWebServer mocky;
    String url;

    @Rule
    public ActivityTestRule<ListActivity> mListActivityRule = new ActivityTestRule<>(ListActivity.class);

    @Before
    public void init() throws IOException {
        mocky = new MockWebServer();
        mocky.start();
        url = mocky.url("/").toString();

    }

    @After
    public void close() throws IOException {
        mocky.close();
    }

    /**
     * Requests compositions from the MockServer
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void loadLists() throws IOException, InterruptedException {
        LocalCache cache = LocalCache.getInstance();
        cache.setServerAddress(url);

        Scanner in = new Scanner(this.getClass().getClassLoader()
                .getResource("compositions.json").openStream());

        String input = "";

        while (in.hasNextLine()) {
            String b = in.nextLine();
            input += b;
        }

        mocky.enqueue(new MockResponse().setResponseCode(200).setBody(input));

        onView(withId(R.id.textView2)).perform(click());

        sleep(1000);
        final ArrayList<Composition> compositions = LocalCache.getInstance().getCompositions(mListActivityRule.getActivity(), LocalCache.ListIdentifier.PUBLIC);
        Assert.assertFalse(compositions.isEmpty());
        Assert.assertTrue(compositions.size() > 0);

    }


}
