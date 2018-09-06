package swarm.swarmcomposerapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * A CompEdge connects two CompNodes.
 *
 */
public class CompEdge {
    private Composition source;
    private Composition target;
    private boolean isCompatible;
    private List<String> alternativesAsText;

    /**
     * Creates a CompEdge without alternatives
     * @param source
     * @param target
     * @param isCompatible
     */
    public CompEdge(Composition source, Composition target, boolean isCompatible){
        this.isCompatible = isCompatible;
        this.source = source;
        this.target = target;
        alternativesAsText = new ArrayList<>();
    }

    /**
     * Creaes a CompEdge with first alternatives
     * @param source
     * @param target
     * @param isCompatible
     * @param firstAlternatives
     */
    public CompEdge(Composition source, Composition target, boolean isCompatible, List<String> firstAlternatives){
        this(source,target,isCompatible);
        this.alternativesAsText = firstAlternatives;
    }

    /**
     * Add one alternative text to the CompNode
     * @param alt
     */
    public void addAlternative(String alt){
        alternativesAsText.add(alt);
    }
    /**
     * Add one alternative texts to the CompNode
     * @param alt
     */
    public void addAlternatives(List<String> alts){
        alternativesAsText.addAll(alts);
    }

    /**
     * Return if the CompEdge indicates compatibility.
     * @return
     */
    public boolean isCompatible(){
        return isCompatible;
    }

    /**
     * Return the target Composition.
     * @return
     */
    public Composition getTarget(){
        return target;
    }

    /**
     * Return the source Composition.
     * @return
     */
    public Composition getSource(){
        return source;
    }

}
