package swarm.swarmcomposerapp.Utils;

import android.util.Log;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import swarm.swarmcomposerapp.Model.LocalCache;


/**
 * Manages the OkHttpClient and the Retrofit client for network communication.
 */
public class RetrofitClients {

    private static Retrofit retrofitClient;
    private static OkHttpClient okHttpClient;

    /**
     * Returns the instance of the Retrofit client.
     *
     * @return
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofitClient == null) {
            newRetrofitInstance(LocalCache.getInstance().getServerAddress());
        }

        return retrofitClient;

    }

    /**
     * Creates a new Retrofit instance. This is used for changing the base url.
     *
     * @param url
     * @return
     */
    public static Retrofit newRetrofitInstance(String url) {
        Retrofit retrofitClientL = new retrofit2.Retrofit.Builder()
                .client(getOkHttpClientInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();
        retrofitClient = retrofitClientL;

        return retrofitClientL;
    }

    /**
     * Returns the instance of the OkHttpClient. It sets a timeout of 5 seconds and is configured for SSL usage.
     *
     * @return
     */
    public static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClient == null) {

            try {

                // TrustManager that ignores who certificated the ssl certificates
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };


                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();

                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });


                builder.connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5, TimeUnit.SECONDS);


                okHttpClient = builder.build();

                return okHttpClient;


            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return okHttpClient;
    }


}
