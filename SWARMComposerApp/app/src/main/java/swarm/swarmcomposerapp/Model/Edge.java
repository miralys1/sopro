package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A Edge connects two CompNodes.
 *
 */
public class Edge {
    @SerializedName("in")
    private Composition in;
    @SerializedName("out")
    private Composition out;
    @SerializedName("compatibilityAnswer")
    private CompatibilityAnswer compatibility;

    public CompatibilityAnswer getCompatibility() {
        return compatibility;
    }

    /**

     * Creates a Edge without alternatives.
     * @param in
     * @param out
     * @param  compatibility
     */
    public Edge(Composition in, Composition out, CompatibilityAnswer  compatibility ){
        this.compatibility  =  compatibility ;
        this.in = in;
        this.out = out;
    }


    /**
     * Return the out Composition.
     * @return
     */
    public Composition getOut(){
        return out;
    }

    /**
     * Return the in Composition.
     * @return
     */
    public Composition getIn(){
        return in;
    }

}
