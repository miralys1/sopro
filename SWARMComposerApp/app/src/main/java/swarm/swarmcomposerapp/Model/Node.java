spackage swarm.swarmcomposerapp.Model;

import android.media.Image;

/**
 * A Node represents a Service within the drawing of a Composition.
 */
public class Node {

    private int x;
    private int y;
    private long serviceID;

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
}
