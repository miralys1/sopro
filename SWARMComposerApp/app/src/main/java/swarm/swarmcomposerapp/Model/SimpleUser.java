package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * SimpleUser that is mainly used for mentioning the author of a composition.
 */
public class SimpleUser {

    @SerializedName("id")
    private long id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;

    @SerializedName("fullName")
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    /**
     * Constructs a simple user.
     * @param id - unic identifying number
     * @param firstName - first name of the user
     * @param lastName - last name of the user
     */
    public SimpleUser(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
