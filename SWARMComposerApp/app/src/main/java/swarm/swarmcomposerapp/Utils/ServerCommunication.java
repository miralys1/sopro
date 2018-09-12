package swarm.swarmcomposerapp.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.POST;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.Service;

/**
 * Proposed class for managing requests to the backend.
 * TODO: Find out if this is compatible with Retrofit.
 * TODO: Implement
 */
public interface ServerCommunication {

    /**
     * Tries a login via POST on /login.
     * It expects credentials in form of an email adresse and a password.
     *
     * @param credentials - email + password
     * @return
     */
    @POST("/login")
    Call<String> login(@Header("Authorization") String credentials);

    /**
     * Requests the list of compositions
     * @return
     */
    @GET("/compositions")
    Call<ArrayList<Composition>> requestList();

    /**
     * Requests the details of a single composition, specified by its @code{id}
     * @param id - an unic identifier
     * @return
     */
    @GET("/compositions/{comp}")
    Call<Composition> requestDetail(@Path("comp") long id);

    /**
     * Requests the list of compositions and expects a @code{token}
     * as credential
     *
     * @param token
     * @param token
     * @return
     */
    @GET("/compositions")
    Call<ArrayList<Composition>> requestListCred(@Header("Authorization") String token);

    /**
     * Requests the details of a composition specified by @code{id} and expects a @code{token}
     * as credential
     *
     * @param token
     * @param id
     * @return
     */
    @GET("/compositions/{comp}")
    Call<Composition> requestDetail(@Header("Authorization") String token, @Path("comp") long id);

    @GET("/services")
    Call<ArrayList<Service>> requestServices();
}
