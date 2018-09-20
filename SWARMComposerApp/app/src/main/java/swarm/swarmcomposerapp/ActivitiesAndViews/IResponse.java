package swarm.swarmcomposerapp.ActivitiesAndViews;

/**
 * IResponse is called by the methods of ActualRequests in order to inform the caller
 * that the answer on its request has arrived or failed.
 */
public interface IResponse {

    /**
     * Called when ServerCommunication receives response corresponding to the senders request.
     *
     * @param successful if request was a success rather than a failure
     */
    void notify(boolean successful);
}
