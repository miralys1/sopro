package swarm.swarmcomposerapp.Model;

import android.media.Image;

/**
 * A CompNode represents a Service within the drawing of a Composition.
 */
public class CompNode {

    private String serviceName;
    private String version;
    private String organisation;
    private String picture;
    private int posX;
    private int posY;
    private boolean isFlexible;
    private long id;
    /**
     * Creates a CompNode with all basic information needed for drawing it.
     *
     * @param posX - X position in the drawing
     * @param posY - Y position in the drawing
     * @param organisation - service's organisation
     * @param picture - name of the image
     * @param serviceName - name of the service
     * @param version - version of the service
     * @param id - internal id
     * @param isFlexible - is the service flexible or strict
     */
    public CompNode(int posX, int posY, String organisation, String picture, String serviceName, String version, long id, boolean isFlexible){
        this.posX = posX;
        this.posY = posY;
        this.organisation = organisation;
        this.picture = picture;
        this.serviceName = serviceName;
        this.version = version;
        this.id = id;
        this.isFlexible = true;
    }

    public String getVersion() {
        return version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getPicture() {
        return picture;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isFlexible() {
        return isFlexible;
    }

    public long getId() {
        return id;
    }
}
