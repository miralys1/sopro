package swarm.swarmcomposerapp.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swarm.swarmcomposerapp.ActivitiesAndViews.IResponse;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.CompositionsAnswer;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Service;


/**
 * ActualRequests separates the Request code made with methods from Retrofit2 from the Activity classes.
 * This allows to swap the connection logic and follows separation of concerns.
 */
public class ActualRequests {
    /**
     * ServerCommunication used for http requests :)
     */
    private static ServerCommunication com = RetrofitClients.getRetrofitInstance().create(ServerCommunication.class);


    public static void refreshServerCommunication() {

        ActualRequests.com = RetrofitClients.getRetrofitInstance()
                .create(ServerCommunication.class);
    }

    /**
     * Starts an asynchronous request for the list of services.
     * The Services are stored in the LocalCache singleton if the request was successful.
     * This method expects a IResponse object to react on its success or failure.
     *
     * @param serviceLookUp - Reference on the HashMap in which the services should be stored
     * @param caller        - the IResponse object desiring to receive the services
     */
    public static void actualServiceRequest(HashMap<Long, Service> serviceLookUp, IResponse caller) {
        Call<ArrayList<Service>> servicesRequest = com.requestServices();

        final HashMap<Long, Service> serviceLookUpL = serviceLookUp;

        final IResponse localCaller = caller;

        servicesRequest.enqueue(new Callback<ArrayList<Service>>()

        {
            @Override
            public void onResponse
                    (Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if (response.isSuccessful()) {
                    for (Service serv : response.body()) {
                        serviceLookUpL.put(serv.getId(), serv);
                    }
                    localCaller.notify(true);
                } else {
                    //Some error has happened

                    localCaller.notify(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                localCaller.notify(false);
            }
        });

    }

    /**
     * Starts an asynchronous request for the list of compositions.
     * The compositions are stored in the LocalCache singleton if the request was successful.
     * This method expects a IResponse object to react on its success or failure.
     *
     * @param publicComps  - Reference on the ArrayList objects in which the compositions should be stored
     * @param ownedComps  - Reference on the ArrayList objects in which the compositions should be stored
     * @param viewableComps  - Reference on the ArrayList objects in which the compositions should be stored
     * @param caller - the IResponse object desiring to receive the compositions
     */
    public static void actualCompListRequest(final ArrayList<Composition> publicComps, final ArrayList<Composition> ownedComps, final ArrayList<Composition> viewableComps, IResponse caller) {
        final Call<CompositionsAnswer> compAnswer;

        LocalCache cacheRef = LocalCache.getInstance();
        String pw = cacheRef.getPassword();
        String mail = cacheRef.getEmail();

        if (pw != null && mail != null && pw != "" && mail != "") {
            compAnswer = com.requestListCred(Credentials.basic(mail, pw));
        } else {
            compAnswer = com.requestList();
        }
        final IResponse localCaller = caller;

        compAnswer.enqueue(new Callback<CompositionsAnswer>() {
            @Override
            public void onResponse(Call<CompositionsAnswer> call,
                                   Response<CompositionsAnswer> response) {


                if (response.isSuccessful()) {
                    publicComps.addAll(response.body().getPublicComps());
                    ownedComps.addAll(response.body().getOwns());
                    viewableComps.addAll(response.body().getViewable());
                    localCaller.notify(true);
                } else {
                    //Some error has happened
                    localCaller.notify(false);
                }
            }

            @Override
            public void onFailure(Call<CompositionsAnswer> call, Throwable t) {
                localCaller.notify(false);
            }
        });
    }

    /**
     * Starts an asynchronous request for a detailed composition.
     * It expects the calling IResponse object to react on the success or failure of its request
     * with @Code{notify(boolean success)}.
     * <p>
     * On success the details of the sent Composition are extracted and added to the @Code{comp}
     *
     * @param comp   - Composition that has been chosen for receiving details
     * @param caller - 'target' the caller has to react on success or failure
     */
    public static void actualCompDetailsRequest(Composition comp, IResponse caller) {
        Call<Composition> compDetails;

        //local variables need to be final to be accessed in OnResponse()
        final Composition tempComp = comp;

        LocalCache cacheRef = LocalCache.getInstance();
        String pw = cacheRef.getPassword();
        String mail = cacheRef.getEmail();

        //Depending whether the user is logged in use the specific request method
        if (pw != null && mail != null) {
            compDetails =
                    com.requestDetail(Credentials.basic(mail, pw), tempComp.getId());
        } else {
            compDetails = com.requestDetail(tempComp.getId());
        }

        final IResponse localResponse = caller;

        compDetails.enqueue(new Callback<Composition>() {
            @Override
            public void onResponse(Call<Composition> call, Response<Composition> response) {
                if (response.isSuccessful()) {

                    /*Add the details of the response's composition to the
                    / tempComp referencing to the Composition that should receive the details */
                    tempComp.addComps(response.body().getNodeList());
                    tempComp.addEdges(response.body().getEdgeList());
                    tempComp.setLastUpdate();
                    localResponse.notify(true);
                } else {
                    //Some error has happened
                    localResponse.notify(false);
                }
            }

            @Override
            public void onFailure(Call<Composition> call, Throwable t) {
                localResponse.notify(false);
            }
        });
    }


}
