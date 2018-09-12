package swarm.swarmcomposerapp.Model;


import com.google.gson.annotations.SerializedName;

/**
 * Format is the internal representation of formats that are used by SWARM plugins.
 */
public class Format {

    @SerializedName("type")
    private String name;
    @SerializedName("version")
    private String version;
    @SerializedName("compatibilityDegree")
    private String compatibilityDegree;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public boolean isFlexible() {
        return compatibilityDegree == "flexible";
    }

    /**
     * Constructs a basic instance of Format.
     *
     * @param name
     * @param version
     * @param isFlexible
     */
    public Format(String name, String version, Boolean isFlexible) {

        this.name = name;
        this.version = version;
        if (isFlexible) {
            this.compatibilityDegree = "flexible";
        } else {
            this.compatibilityDegree = "strict";
        }
    }

}
