package swarm.swarmcomposerapp.Utils;

import java.util.ArrayList;
import java.util.List;

import swarm.swarmcomposerapp.Model.Composition;

/**
 * Proposed class for managing requests to the backend.
 * TODO: Find out if this is compatible with Retrofit.
 * TODO: Implement
 */
public class ServerCommunication {

    public static ArrayList<Composition> requestList(){
        return (new ArrayList<>());
    }

    public static Composition requestDetail(long id){
        //TODO impement
        return null;
    }
}
