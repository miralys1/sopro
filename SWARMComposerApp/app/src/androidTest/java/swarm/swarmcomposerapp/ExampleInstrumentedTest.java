package swarm.swarmcomposerapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import net.bytebuddy.asm.Advice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import swarm.swarmcomposerapp.ActivitiesAndViews.IResponse;
import swarm.swarmcomposerapp.ActivitiesAndViews.MainActivity;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.Model.Settings;
import swarm.swarmcomposerapp.Utils.ActualRequests;
import swarm.swarmcomposerapp.Utils.RetrofitClients;
import swarm.swarmcomposerapp.Utils.ServerCommunication;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        Settings.getInstance().setServerAdress(server.url("/").toString());
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(Context context, String filePath) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);

        String ret = convertStreamToString(stream);
        stream.close();
        return ret;
    }


    @Test
    public void test() throws IOException {
        server = new MockWebServer();
        server.start();
        Settings.getInstance().setServerAdress(server.url("/").toString());


        try {
            String str = this.getStringFromFile(InstrumentationRegistry.getContext(), "products.json");

            server.enqueue(new MockResponse().setResponseCode(200).setBody(str));
            final ServerCommunication com = RetrofitClients.getRetrofitInstance().create(ServerCommunication.class);

            Call<ArrayList<Service>> servicesRequest = com.requestServices();

            final HashMap<Long, Service> serviceLookUpL = new HashMap<Long,Service>();


            servicesRequest.enqueue(new Callback<ArrayList<Service>>()

            {
                @Override
                public void onResponse
                        (Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                    if (response.isSuccessful()) {
                        for (Service serv : response.body()) {
                            serviceLookUpL.put(serv.getId(), serv);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                }
            });



        } catch (Exception e) {
        System.out.println("Exception whoops");
        }

        server.shutdown();
    }


}
