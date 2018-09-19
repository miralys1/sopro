package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Alternative {
    public List<String> getNames() {
        return names;
    }

    @SerializedName("names")
    List<String> names;
    @SerializedName("versions")
    List<String> versions;
    @SerializedName("ids")
    List<String> ids;
}
