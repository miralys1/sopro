package swarm.swarmcomposerapp.Model;

/**
 * A CompNode represents a Service within the drawing of a Composition.
 */
public class CompNode {

    private String serviceName;
    private String info;
    private long imgID;
    private int posX;
    private int posY;

    /**
     * Creates a CompNode with all basic information needed for drawing it.
     *
     * @param posX - X position in the drawing
     * @param posY - Y position in the drawing
     * @param info - info text of the service
     * @param imgID - ID of the image
     * @param serviceName - name of the service
     */
    public CompNode(int posX, int posY, String info, long imgID, String serviceName){
        this.posX = posX;
        this.posY = posY;
        this.info = info;
        this.imgID = imgID;
        this.serviceName = serviceName;
    }



}
