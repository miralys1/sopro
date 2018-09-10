package swarm.swarmcomposerapp.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import swarm.swarmcomposerapp.Model.Settings;

public class RetrofitClients {

    private static Retrofit retrofitClient;
    private static OkHttpClient okHttpClient

    public static Retrofit getRetrofitInstance(){
        if(retrofitClient == null){
            retrofitClient = new retrofit2.Retrofit.Builder()
                    .baseUrl(Settings.getInstance()
                            .getServerAdress()).addConverterFactory(GsonConverterFactory.create())
                            .build();
        }

        return retrofitClient;
    }

    public static OkHttpClient getOkHttpClientInstance(){
        if(okHttpClient == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            okHttpClient = okHttpClient.newBuilder().build();
        }
        return okHttpClient;
    }

}
