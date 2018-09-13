package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompositionsAnswer {
    @SerializedName("editable")
    List<Composition> editable;
    @SerializedName("viewable")
    List<Composition> viewable;
    @SerializedName("publicComps")
    List<Composition> publicComps;

    public List<Composition> getEditable() {
        return editable;
    }

    public List<Composition> getViewable() {
        return viewable;
    }

    public List<Composition> getPublicComps() {
        return publicComps;
    }
}
