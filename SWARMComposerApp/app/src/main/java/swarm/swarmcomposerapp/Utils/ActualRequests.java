package swarm.swarmcomposerapp.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swarm.swarmcomposerapp.ActivitiesAndViews.IResponse;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Service;

public class ActualRequests {
    /**
     * ServerCommunication used for http requests :)
     */
    private static ServerCommunication com = refreshServerCommunication();

    public static ServerCommunication refreshServerCommunication() {
        return RetrofitClients.getRetrofitInstance().create(ServerCommunication.class);
    }


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
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                localCaller.notify(false);
            }
        });

    }

    public static void actualCompListRequest(ArrayList<Composition> comps, IResponse caller) {
        final Call<ArrayList<Composition>> compList;

        LocalCache cacheRef = LocalCache.getInstance();
        String pw = cacheRef.getPassword();
        String mail = cacheRef.getEmail();

        if (pw != null && mail != null) {
            compList = com.requestListCred(Credentials.basic(mail, pw));
        } else {
            compList = com.requestList();
        }
        final IResponse localCaller = caller;
        final ArrayList<Composition> compsL = comps;
        compList.enqueue(new Callback<ArrayList<Composition>>() {
            @Override
            public void onResponse(Call<ArrayList<Composition>> call,
                                   Response<ArrayList<Composition>> response) {
                if (response.isSuccessful()) {
                    compsL.addAll(response.body());
                    localCaller.notify(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Composition>> call, Throwable t) {
                localCaller.notify(false);
            }
        });
    }

    public static void actualCompDetailsRequest(Composition comp, IResponse caller) {
        Call<Composition> compDetails;

        LocalCache localSettingsRef = LocalCache.getInstance();

        final Composition tempComp = comp;

        LocalCache cacheRef = LocalCache.getInstance();
        String pw = cacheRef.getPassword();
        String mail = cacheRef.getEmail();

        if (pw != null && mail != null) {
            //Depending whether the user is logged in use the specific request method
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
                    tempComp.addComps(response.body().getNodeList());
                    tempComp.addEdges(response.body().getEdgeList());
                    tempComp.setLastUpdate();
                    localResponse.notify(true);
                }
            }

            @Override
            public void onFailure(Call<Composition> call, Throwable t) {

                localResponse.notify(false);
            }
        });
    }


}
