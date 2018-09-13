package swarm.swarmcomposerapp.Model;

import java.util.ArrayList;
import java.util.HashMap;

import swarm.swarmcomposerapp.ActivitiesAndViews.IResponse;
import swarm.swarmcomposerapp.Utils.ActualRequests;
import swarm.swarmcomposerapp.Utils.RetrofitClients;

/**
 * LocalCache manages compositions and services and hides its basic structure behind specific methods.
 * It's implemented as an eager singleton.
 */

public class LocalCache implements ICache {

    private String email;
    private String password;
    private String serverAdress = "http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerAdress() {
        return serverAdress;
    }

    public void setServerAdress(String serverAdress) {
        this.serverAdress = serverAdress;

        RetrofitClients.newRetrofitInstance(serverAdress);
    }

    private static LocalCache instance = new LocalCache();
    private ArrayList<Composition> compositions = new ArrayList();
    private HashMap<Long, Service> serviceLookUp = new HashMap<>();


    @Override
    public Service[] getServices(IResponse caller) {
        if (serviceLookUp.isEmpty()) {

            ActualRequests.actualServiceRequest(serviceLookUp, caller);
            return null;
        } else {
            return (Service[]) serviceLookUp.values().toArray();
        }

    }

    @Override
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

    @Override
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
            ActualRequests.actualCompDetailsRequest(tempComp, caller);
            return null;
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
            return null;
        }
        return compositions;
    }

    /**
     * Requests the composition list from the backend and replaces the old list completely
     * without checking for changes.
     */
    public void hardRefresh(IResponse caller) {
        ActualRequests.actualCompListRequest(compositions, caller);
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
