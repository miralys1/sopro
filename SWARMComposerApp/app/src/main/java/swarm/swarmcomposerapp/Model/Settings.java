package swarm.swarmcomposerapp.Model;

/**
 * Proposed model for storing settings.
 * TODO: Find out if this is even needed or solved by Preferences
 */
public class Settings {

    private String email;
    private String token;
    private String serverAdress
    private static Settings instance = new Settings();

    private Settings(){
    }

    public static Settings getInstance(){
        return instance;
    }

}
