package swarm.swarmcomposerapp.Model;

import android.net.Credentials;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swarm.swarmcomposerapp.Utils.RetrofitClients;
import swarm.swarmcomposerapp.Utils.ServerCommunication;
import okhttp3.Credentials;

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
    private ServerCommunication com = RetrofitClients.getRetrofitInstance().create(ServerCommunication.class);



    /**
     * Tries to receive a service from the LocalCache by its id.
     * It doesn't request it from the backend if its null at the moment!
     * Be careful!
     *
     * @param id
     * @return
     */
    public Service getServiceById(long id) {
        return serviceLookUp.get(id);
    }

    /**
     * Returns the composition at a specific position in the composition list.
     * If the composition has not received its details then it will request them from the backend.
     *
     * @param pos
     * @return
     */
    public Composition getCompAtPos(int pos) throws IllegalArgumentException {

        if (compositions.size() < pos) {
            throw new IllegalArgumentException("This request would lead to an index out of " +
                    "bounds exception!");
        }

        if (pos < 0) {
            throw new IllegalArgumentException("Something went wrong: A list has no positions < 0.");
        }

        Composition tempComp = compositions.get(pos);

        if (tempComp == null) {
            throw new CompositionExpectedException("Something went wrong: The element " +
                    "at this position is null.");
        }

        if (tempComp.getNodeList().isEmpty()) {
            Call<Composition> compDetails;

            if(settings.getToken() != null){
                compDetails =
                        com.requestDetail(settings.getToken(), tempComp.getId());
            }else{
                compDetails = com.requestDetail(tempComp.getId());
            }
            compDetails.enqueue(new Callback<Composition>() {
                @Override
                public void onResponse(Call<Composition> call, Response<Composition> response) {

                }

                @Override
                public void onFailure(Call<Composition> call, Throwable t) {

                }
            });


            //Composition n = ServerCommunication.requestDetail(tempComp.getId());

            //tempComp.addComps(n.getNodeList());
            //tempComp.addEdges(n.getEdgeList());
        }
        return tempComp;
    }

    /**
     * Returns the compositions.
     * If there hasn't been any requests yet, it will request the composition list from backend.
     *
     * @return
     */
    public ArrayList<Composition> getCompositions() {
        if (compositions.isEmpty()) {
            hardRefresh();
        }
        return compositions;
    }

    /**
     * Requests the composition list from the backend and replaces the old list completely
     * without checking for changes.
     */
    public void hardRefresh() {
       //this.compositions = ServerCommunication.requestList();

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
