package swarm.swarmcomposerapp.Model;


import com.google.gson.annotations.SerializedName;

/**
 * A Node represents a Service within the drawing of a Composition.
 */
public class Node {

    @SerializedName("x")
    private int x;
    @SerializedName("y")
    private int y;
    private long serviceID;

    @SerializedName("sendService")
    private Service sendService;

    /**
     * A service is mapped to an instance of node.
     * A node always contains coordinates and the id of its service.
     *
     * @param x
     * @param y
     * @param serviceID
     */
    public Node(int x, int y, long serviceID) {
        this.x = x;
        this.y = y;
        this.serviceID = serviceID;
    }

    public Node(int x, int y, Service sendService){
        this.x = x;
        this.y = y;
        this.sendService = sendService;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setServiceID(long serviceID) {
        this.serviceID = serviceID;
    }

    public int getX() {

        return x;
    }

    public int getY() {
        return y;
    }

    public long getServiceID() {
        return serviceID;
    }


    public Service getSendService() {
        return sendService;
    }

    public void setSendService(Service sendService) {
        this.sendService = sendService;
    }


}
