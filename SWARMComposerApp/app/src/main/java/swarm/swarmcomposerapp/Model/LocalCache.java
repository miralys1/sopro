package swarm.swarmcomposerapp.Model;

import android.app.Activity;
import android.net.Credentials;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swarm.swarmcomposerapp.ActivitiesAndViews.IResponse;
import swarm.swarmcomposerapp.Utils.RetrofitClients;
import swarm.swarmcomposerapp.Utils.ServerCommunication;

/**
 * LocalCache manages compositions and services and hides its basic structure behind specific methods.
 * It's implemented as an eager singleton.
 */

public class LocalCache implements ICache {


    private static LocalCache instance = new LocalCache();
    private ArrayList<Composition> compositions = new ArrayList();
    private HashMap<Long, Service> serviceLookUp = new HashMap<>();

    /**
     * local reference on the settings instance
     */
    private Settings settings = Settings.getInstance();

    /**
     * ServerCommunication used for http requests :)
     */
    private ServerCommunication com = refreshServerCommunication();

    public ServerCommunication refreshServerCommunication(){
        return RetrofitClients.getRetrofitInstance().create(ServerCommunication.class);
    }
    @Override
    public Service[] getServices(IResponse caller) {
        if(serviceLookUp.isEmpty()){
            Call<ArrayList<Service>> servicesRequest = com.requestServices();
            final IResponse localCaller = caller;
            servicesRequest.enqueue(new Callback<ArrayList<Service>>() {
                @Override
                public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                    if(response.isSuccessful()){
                     for(Service serv : response.body()){
                         serviceLookUp.put(serv.getId(),serv);
                     }
                    localCaller.notify(true);}
                }

                @Override
                public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                    localCaller.notify(false);
                }
            });

        }
        return (Service[])serviceLookUp.values().toArray();

    }

    /**
     * Tries to receive a service from the LocalCache by its id.
     * It doesn't request it from the backend if its null at the moment!
     * Be careful!
     *
     * @param id
     * @return
     */
    public Service getServiceById(long id, IResponse caller) {
        return serviceLookUp.get(id);
    }

    /**
     * Returns the composition at a specific position in the composition list.
     * If the composition has not received its details then it will request them from the backend.
     *
     * @param pos
     * @return
     */
    public Composition getCompAtPos(int pos, IResponse caller) throws IllegalArgumentException {

        if (compositions.size() < pos) {
            throw new IllegalArgumentException("This request would lead to an index out of " +
                    "bounds exception!");
        }

        if (pos < 0) {
            throw new IllegalArgumentException("Something went wrong: A list has no positions < 0.");
        }

        final Composition tempComp = compositions.get(pos);

        if (tempComp == null) {
            throw new CompositionExpectedException("Something went wrong: The element " +
                    "at this position is null.");
        }

        if (tempComp.getNodeList().isEmpty()) {
            Call<Composition> compDetails;

            //Depending whether the user is logged in use the specific request method
            if (settings.getToken() != null) {
                compDetails =
                        com.requestDetail(settings.getToken(), tempComp.getId());
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
        return tempComp;
    }

    /**
     * Returns the compositions.
     * If there hasn't been any requests yet, it will request the composition list from backend.
     *
     * @return
     */
    public ArrayList<Composition> getCompositions(IResponse caller) {
        if (compositions.isEmpty()) {
            hardRefresh(caller);
        }
        return compositions;
    }

    /**
     * Requests the composition list from the backend and replaces the old list completely
     * without checking for changes.
     */
    public void hardRefresh(IResponse caller) {
        Call<ArrayList<Composition>> compList;

        if(Settings.getInstance().getToken() != null){
            compList = com.requestListCred(Settings.getInstance().getToken());
        }else{
            compList = com.requestList();
        }
        final IResponse localCaller = caller;
        compList.enqueue(new Callback<ArrayList<Composition>>() {
            @Override
            public void onResponse(Call<ArrayList<Composition>> call,
                                   Response<ArrayList<Composition>> response) {
                if(response.isSuccessful()){
                    compositions = response.body();
                    localCaller.notify(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Composition>> call, Throwable t) {
                localCaller.notify(false);
            }
        });

    }


    /**
     * LocalCache is a singleton. Calling the constructor is prohibited.
     */
    private LocalCache() {
    }

    /**
     * Returns the instance of the LocalCache.
     *
     * @returns
     */
    public static LocalCache getInstance() {
        return instance;
    }


}
