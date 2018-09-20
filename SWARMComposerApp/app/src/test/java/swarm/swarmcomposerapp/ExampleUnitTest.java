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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

/**
 * Tests whether our Retrofit interface is correctly defined. Additionally, it indirectly tests whether an
 * old products.json is  parsed to a list of services.
 * Besides, this test was really useful in order to understand how Retrofit requests work.
 */
public class ExampleUnitTest {


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
    public void test() throws IOException, InterruptedException {
        System.out.println("Begin of test: ");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS);


        Retrofit retrofit = new Retrofit.Builder().client(builder.build())
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
        mocky.enqueue(new MockResponse().setResponseCode(200).setBody(input).setBodyDelay(2, TimeUnit.SECONDS));
        mocky.enqueue(new MockResponse().setResponseCode(200).setBody(input).setBodyDelay(2, TimeUnit.SECONDS));

        Call<ArrayList<Service>> servicesRequest = com.requestServices();

        final HashMap<Long, Service> serviceLookUpL = new HashMap<Long, Service>();

        System.out.println("Enqueue Callback");
        Response<ArrayList<Service>> execute = servicesRequest.execute();
        Assert.assertNotNull(execute.body());
        System.out.println(execute.body().get(0).getServiceName());

    }


}