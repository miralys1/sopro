package swarm.swarmcomposerapp.Model;


/**
 * Format is the internal representation of formats that are used by SWARM plugins.
 */
public class Format {

    private String name;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public boolean isFlexible() {
        return isFlexible;
    }

    /**
     * Constructs a basic instance of Format.
     *
     * @param name
     * @param version
     * @param isFlexible
     */
    public Format(String name, String version, boolean isFlexible) {

        this.name = name;
        this.version = version;
        this.isFlexible = isFlexible;
    }

    private String version;
    private boolean isFlexible;
}
