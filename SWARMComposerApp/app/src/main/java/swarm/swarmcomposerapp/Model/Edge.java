package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;


/**
 * A Edge connects two CompNodes.
 *
 */
public class Edge {
    @SerializedName("target")
    private Node in;

    @SerializedName("source")
    private Node out;

    @SerializedName("compatibility")
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
    public Edge(Node in, Node out, CompatibilityAnswer  compatibility ){
        this.compatibility  =  compatibility ;
        this.in = in;
        this.out = out;
    }


    /**
     * Return the out Composition.
     * @return
     */
    public Node getOut(){
        return out;
    }

    /**
     * Return the in Composition.
     * @return
     */
    public Node getIn(){
        return in;
    }

}
