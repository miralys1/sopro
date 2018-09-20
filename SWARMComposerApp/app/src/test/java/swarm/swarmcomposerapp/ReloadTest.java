package swarm.swarmcomposerapp;

import android.app.Activity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import swarm.swarmcomposerapp.ActivitiesAndViews.ListActivity;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Utils.ActualRequests;

import static java.lang.Thread.sleep;

@RunWith(RobolectricTestRunner.class)
public class ReloadTest {
    MockWebServer mocky;
    String url;
    ListActivity activity;

    @Before
    public void init() throws IOException {
        mocky = new MockWebServer();
        mocky.start();
        url = mocky.url("/").toString();

        activity = Robolectric.buildActivity(ListActivity.class).create().start().get();
    }

    @After
    public void close() throws IOException {
        mocky.close();
    }


    /**
     * Tests whether pressing the reload buttons sends a request at all.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void refreshRequestTest() throws IOException, InterruptedException {
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


        activity.findViewById(R.id.textView2).performClick();
        sleep(1000);
        Assert.assertTrue(mocky.getRequestCount() == 1);
    }


}
