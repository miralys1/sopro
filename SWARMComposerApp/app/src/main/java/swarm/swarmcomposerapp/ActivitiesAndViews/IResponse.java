package swarm.swarmcomposerapp.ActivitiesAndViews;


public interface IResponse {

    /**
     * Called when ServerCommunication receives response corresponding to the senders request.
     * @param successful if request was a success rather than a failure
     */
     void notify(boolean successful);
}
