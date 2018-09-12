package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Alternative {
    @SerializedName("name")
    List<String> names;
    @SerializedName("version")
    List<String> versions;
    @SerializedName("id")
    List<String> ids;
}
