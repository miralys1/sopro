package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model representation of a service.
 */
public class Service {
    @SerializedName("name")
    private String serviceName;
    @SerializedName("version")
    private String version;
    @SerializedName("organisation")
    private String organisation;
    @SerializedName("logo")
    private String picture;
    @SerializedName("id")
    private long id;
    @SerializedName("tags")
    private List<String> tags;
    @SerializedName("formatIn")
    private List<Service> in;
    @SerializedName("formatOut")
    private List<Service> out;

    /**
     * Constructs a service with all needed information.
     *
     * @param serviceName
     * @param version
     * @param organisation
     * @param picture
     * @param id
     * @param date
     * @param tags
     * @param in
     * @param out
     */
    public Service(String serviceName, String version, String organisation,
                   String picture, long id, long date, List<String> tags, List<Service> in,
                   List<Service> out) {
        this.serviceName = serviceName;
        this.version = version;
        this.organisation = organisation;
        this.picture = picture;
        this.id = id;
        this.date = date;
        this.tags = tags;
        this.in = in;
        this.out = out;
    }

    private long date;


    public void setDate(long date) {
        this.date = date;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setIn(List<Service> in) {
        this.in = in;
    }

    public void setOut(List<Service> out) {
        this.out = out;
    }


    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getVersion() {
        return version;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getPicture() {
        return picture;
    }

    public long getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<Service> getIn() {
        return in;
    }

    public List<Service> getOut() {
        return out;
    }

}
