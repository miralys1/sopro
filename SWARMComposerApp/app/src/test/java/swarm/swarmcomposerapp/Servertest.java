package swarm.swarmcomposerapp;

import android.net.Credentials;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.CompositionsAnswer;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.Utils.ServerCommunication;

import static org.junit.Assert.assertNotNull;

/**
 * Basic connection tests using Retrofit code and our interface
 */
public class Servertest {


    @Test
    public void test2() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.245.1.240:9061/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ServerCommunication com = retrofit.create(ServerCommunication.class);
        final HashMap<Long, Service> serviceLookUpL = new HashMap<Long, Service>();
        Call<ArrayList<Service>> servicesRequest = com.requestServices();

        System.out.println("Enqueue Callback2");
        Response<ArrayList<Service>> execute = servicesRequest.execute();
        assertNotNull(execute);
        System.out.println(execute.message());
        System.out.println(execute.body().get(0).getServiceName());
        System.out.println(execute.body().get(1).getServiceName());
    }

    @Test
    public void test3() throws IOException {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder().client(builder.build())
                .baseUrl("http://134.245.1.240:9061/composer-0.0.1-AUTH/").addConverterFactory(GsonConverterFactory.create())
                .build();




        final ServerCommunication com = retrofit.create(ServerCommunication.class);
        final ArrayList<Composition> comps = new ArrayList<>();
        final Call<CompositionsAnswer> arrayListCall = com.requestListCred(okhttp3.Credentials.basic("d@d.de","123"));


        LocalCache.getInstance().setEmail("d@d.de");
        LocalCache.getInstance().setPassword("123");

        System.out.println("Enqueue Callback2");
        Response<CompositionsAnswer> execute = arrayListCall.execute();
        System.out.println(arrayListCall.request() +" auth "+arrayListCall.request().header("Authorization")
        );

        assertNotNull(execute);
        System.out.println(execute.message());
        System.out.println(execute.body().getPublicComps().get(0).getName());
    }
}
