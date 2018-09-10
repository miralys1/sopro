package swarm.swarmcomposerapp.Model;

import java.util.ArrayList;

/**
 * ICache presents methods that should be used by our own Cache implementation.
 */
public interface ICache {

    Service getServiceById(long id);

    Composition getCompAtPos(int pos);

    ArrayList<Composition> getCompositions();

    void hardRefresh();

    LocalCache getInstance();
}
