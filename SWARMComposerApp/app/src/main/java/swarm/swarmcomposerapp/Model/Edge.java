package swarm.swarmcomposerapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * A Edge connects two CompNodes.
 *
 */
public class Edge {
    private Composition in;
    private Composition out;
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
