package swarm.swarmcomposerapp.Model;

import android.app.Activity;

import java.util.ArrayList;

import swarm.swarmcomposerapp.ActivitiesAndViews.IResponse;

/**
 * ICache presents methods that should be used by our own Cache implementation.
 */
public interface ICache {

    Service getServiceById(long id, IResponse caller);

    Composition getCompAtPos(int pos, IResponse caller, int listID);

    ArrayList<Composition> getCompositions(IResponse caller);

    void hardRefresh(IResponse caller);

    Service[] getServices(IResponse caller);

}
