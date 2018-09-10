package swarm.swarmcomposerapp.Model;

/**
 * Proposed model for storing settings.
 * TODO: Find out if this is even needed or solved by Preferences
 */
public class Settings {

    private String email;
    private String token;
    private String serverAdress = "https://134.245.1240:9060";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServerAdress() {
        return serverAdress;
    }

    public void setServerAdress(String serverAdress) {
        this.serverAdress = serverAdress;
    }

    public static void setInstance(Settings instance) {
        Settings.instance = instance;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getEmail() {

        return email;
    }

    private static Settings instance = new Settings();

    private Settings(){
    }

    public static Settings getInstance(){
        return instance;
    }

}
