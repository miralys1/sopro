package swarm.swarmcomposerapp.Model;

/**
 * SimpleUser that is mainly used for mentioning the author of a composition.
 */
public class SimpleUser {

    private long id;
    private String firstName;
    private String lastName;

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
