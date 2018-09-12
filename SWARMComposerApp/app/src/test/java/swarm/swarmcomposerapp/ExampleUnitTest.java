package swarm.swarmcomposerapp;

import android.content.Context;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.Utils.RetrofitClients;
import swarm.swarmcomposerapp.Utils.ServerCommunication;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    MockWebServer mocky;
    String url;

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




    @Test
    public void test() throws IOException {
        System.out.println("Begin of test: ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build();

        final ServerCommunication com = retrofit.create(ServerCommunication.class);

        Scanner in = new Scanner(this.getClass().getClassLoader()
                .getResource("products.json").openStream());

        String input = "";

        while (in.hasNextLine()) {
            String b = in.nextLine();
            input += b;
        }

        System.out.println("Begin of response enqueue");
        mocky.enqueue(new MockResponse().setResponseCode(200).setBody(input));


        Call<ArrayList<Service>> servicesRequest = com.requestServices();

        final HashMap<Long, Service> serviceLookUpL = new HashMap<Long, Service>();

        System.out.println("Enqueue Callback");
        Response<ArrayList<Service>> execute = servicesRequest.execute();
        Assert.assertNotNull(execute.body());
        System.out.println(execute.body().get(0).getServiceName());

    }


}