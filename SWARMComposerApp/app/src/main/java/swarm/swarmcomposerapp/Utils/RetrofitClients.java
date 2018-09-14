package swarm.swarmcomposerapp.Utils;

import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import swarm.swarmcomposerapp.Model.LocalCache;

import static android.support.constraint.Constraints.TAG;

public class RetrofitClients {

    private static Retrofit retrofitClient;
    private static OkHttpClient okHttpClient;

    public static Retrofit getRetrofitInstance() {
        if (retrofitClient == null) {
            newRetrofitInstance(LocalCache.getInstance().getServerAddress());
        }

        return retrofitClient;

    }

    public static Retrofit newRetrofitInstance(String url) {
        Retrofit retrofitClientL = new retrofit2.Retrofit.Builder()
                .client(getOkHttpClientInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();
        Log.i("ServerAddressRetrofitCl", "set to: " + url);

        retrofitClient = retrofitClientL;

        return retrofitClientL;
    }
    private static Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            String content = response.request().toString();
            Log.d(TAG,  content);
            return chain.proceed(chain.request());
        }
        catch (Exception exception) {
            exception.printStackTrace();

        }

        return chain.proceed(chain.request());
    }

    public static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();


            builder.connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS);
//                    .interceptors().add(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    return onOnIntercept(chain);
//                }
//            });
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }







}
