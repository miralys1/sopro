package swarm.swarmcomposerapp.Model;

import java.util.ArrayList;
import java.util.HashMap;
s
import swarm.swarmcomposerapp.Utils.ServerCommunication;

/**
 * LocalCache manages compositions and services and hides its basic structure behind specific methods.
 * It's implemented as an eager singleton.
 */
public class LocalCache {

    private static LocalCache instance = new LocalCache();
    private ArrayList<Composition> compositions = new ArrayList();
    private HashMap<Long, Service> serviceLookUp = new HashMap<>();

    /**
     * Tries to receive a service from the LocalCache by its id.
     * It doesn't request it from the backend if its null at the moment!
     * Be careful!
     * @param id
     * @return
     */
    private Service getServiceById(long id) {
        return serviceLookUp.get(id);
    }

    /**
     * Returns the composition at a specific position in the composition list.
     * If the composition has not received its details then it will request them from the backend.
     *
     * @param pos
     * @return
     */
    private Composition getCompAtPos(int pos) throws IllegalArgumentException {

        if(compositions.size()< pos){
            throw new IllegalArgumentException("This request would lead to an index out of " +
                    "bounds exception!");
        }

        if(pos < 0){
            throw new IllegalArgumentException("Something went wrong: A list has no positions < 0.");
        }

        Composition tempComp = compositions.get(pos);

        if(tempComp == null){
            throw new CompositionExpectedException("Something went wrong: The element " +
                    "at this position is null.");
        }

        if (tempComp.getNodeList().isEmpty()) {
            ServerCommunication.requestDetail(tempComp.getId());
        }

    }

    /**
     * Returns the compositions.
     * If there hasn't been any requests yet, it will request the composition list from backend.
     *
     * @return
     */
    public ArrayList<Composition> getCompositions() {
        if (compositions.isEmpty()) {
            this.compositions = ServerCommunication.requestList();
        }
        return compositions;
    }

    /**
     * LocalCache is a singleton. Calling the constructor is prohibited.
     */
    private LocalCache() {
    }

    /**
     * Returns the instance of the LocalCache.
     * @returns
     */
    public LocalCache getInstance() {
        return instance;
    }


}
