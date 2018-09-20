package swarm.swarmcomposerapp.Model;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private long lastUpdate;
    //public static final String TEST_SERVER = "http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/";
    public static final String TEST_SERVER = "http://134.245.1.240:9061/composer-0.0.1-AUTH/";
    private String serverAddress = TEST_SERVER;
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");

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

    public String getServerAddress() {
        return serverAddress;
    }

    public String getLastUpdate() {
        return dateFormat.format(new Date(lastUpdate));
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        Log.i("ServerAddressLocalCache", "set to: " + serverAddress);
        RetrofitClients.newRetrofitInstance(serverAddress);
        ActualRequests.refreshServerCommunication();
    }

    private static LocalCache instance = new LocalCache();
    private ArrayList<Composition> compositions = new ArrayList(); //TODO remove
    private ArrayList<Composition> publicComps = new ArrayList();
    private ArrayList<Composition> viewableComps = new ArrayList();
    private ArrayList<Composition> ownedComps = new ArrayList();
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
    public Composition getCompAtPos(int pos, IResponse caller, int listID) throws IllegalArgumentException {

//        if (compositions.size() < pos) {
//            throw new IllegalArgumentException("This request would lead to an index out of " +
//                    "bounds exception!");
//        }

        if (pos < 0) {
            throw new IllegalArgumentException("Something went wrong: A list has no positions < 0.");
        }


        Log.i("DETAIL", "in cache: getCompAtPos " + pos + " in list " + listID);
        switch (listID) {
            case 2:
                posChecker(publicComps, pos);
                return compositionRetrieveHelper(publicComps, pos, caller);

            case 1:
                posChecker(viewableComps, pos);
                return compositionRetrieveHelper(viewableComps, pos, caller);


            case 0:
                posChecker(ownedComps, pos);
                return compositionRetrieveHelper(ownedComps, pos, caller);

            default:
                return null;
        }
    }

    /**
     * Checks if the demanded position exceeds the range of a List
     *
     * @param list
     * @param pos
     */
    public static void posChecker(ArrayList list, int pos) {
        if (list.size() - 1 < pos) {
            throw new IllegalArgumentException("This request would lead to an index out of " +
                    "bounds exception!");
        }
    }

    /**
     * Starts the actual request for a specific compositions list
     *
     * @param specificCompList
     * @param pos
     * @param caller
     * @return
     */
    public Composition compositionRetrieveHelper(List<Composition> specificCompList, int pos, IResponse caller) {
        final Composition tempOwnedComp = specificCompList.get(pos);
        if (tempOwnedComp.getNodeList() == null || tempOwnedComp.getNodeList().isEmpty()) {
            ActualRequests.actualCompDetailsRequest(tempOwnedComp, caller);

            //Comp is not detailed, but this is the matter of the detail activity
            return tempOwnedComp;
        } else {
            //Comp is detailed
            return tempOwnedComp;
        }


    }


    /**
     * Returns the compositions.
     * If there hasn't been any requests yet, it will request the composition list from backend.
     *
     * @return
     */
    public ArrayList<Composition> getCompositions(IResponse caller) {
        //TODO also hand over an enum which represents one of the four lists. Return the needed ArrayList. Only hardRefresh if public list is empty.
        if (compositions.isEmpty()) {
            hardRefresh(caller);
            return null;
        }
        return compositions;
    }

    public ArrayList<Composition> getPublicComps() {
        return publicComps;
    }

    public ArrayList<Composition> getViewableComps() {
        return viewableComps;
    }

    public ArrayList<Composition> getOwnedComps() {
        return ownedComps;
    }

    /**
     * Returns the compositions.
     * If there hasn't been any requests yet, it will request the composition list from backend.
     *
     * @return
     */
    public ArrayList<Composition> getCompositions(IResponse caller, ListIdentifier listIdentifier) {
        if (!hasData() && listIdentifier == ListIdentifier.PUBLIC) {
            hardRefresh(caller);
            return null;
        } else {
            switch (listIdentifier) {
                case PUBLIC:
                    return publicComps;
                case OWNED:
                    return ownedComps;
                case VIEWABLE:
                    return viewableComps;
            }
        }
        return compositions;
    }

    /**
     * Requests the composition list from the backend and replaces the old list completely
     * without checking for changes.
     */
    public void hardRefresh(IResponse caller) {
        lastUpdate = System.currentTimeMillis();
        Log.i("AddressHardRefresh", "Caller: " + caller.getClass().getName());

        publicComps = new ArrayList<>();
        ownedComps = new ArrayList<>();
        viewableComps = new ArrayList<>();

        ActualRequests.actualCompListRequest(publicComps, ownedComps, viewableComps, caller);
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

    public enum ListIdentifier {
        PUBLIC, OWNED, VIEWABLE;
    }

    /**
     *
     * @return
     */
    public boolean hasData() {
        return ((publicComps != null && !publicComps.isEmpty()) || (viewableComps != null && !viewableComps.isEmpty()) || (ownedComps != null && !ownedComps.isEmpty()));
    }

}
