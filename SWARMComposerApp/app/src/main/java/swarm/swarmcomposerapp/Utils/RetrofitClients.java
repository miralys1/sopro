package swarm.swarmcomposerapp.Utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import swarm.swarmcomposerapp.Model.LocalCache;

public class RetrofitClients {

    private static Retrofit retrofitClient;
    private static OkHttpClient okHttpClient;

    public static Retrofit getRetrofitInstance(){
        if(retrofitClient == null){
            retrofitClient = newRetrofitInstance(LocalCache.getInstance().getServerAddress());
        }

        return retrofitClient;

    }
    public static Retrofit newRetrofitInstance(String url){
        Retrofit retrofitClientL = new retrofit2.Retrofit.Builder()
                .client(getOkHttpClientInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();
        Log.i("ServerAddressRetrofitCl","set to: "+url);
        retrofitClient = retrofitClientL;

        return retrofitClientL;
    }


    public static OkHttpClient getOkHttpClientInstance(){
        if(okHttpClient == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS);
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

}
