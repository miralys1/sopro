package swarm.swarmcomposerapp.Model;

import java.util.List;

public class Composition {

    //private long authorID;
    private long id;
    private String owner;
    private long lastChange;
    List<CompNode> compNodeList;
    List<CompEdge> compEdgeList;
    boolean editable;


    /**
     * Add one compNode to the CompNodeList
     *
     * @param comp
     */
    public void addComp(CompNode comp) {
        compNodeList.add(comp);
    }

    /**
     * Add compNodes to the compNodeList
     *
     * @param comps
     */
    public void addComps(List<CompNode> comps) {
        compNodeList.addAll(comps);
    }


    /**
     * Add one compEdge to the compEdgeList.
     *
     * @param edge
     */
    public void addEdge(CompEdge edge) {
        compEdgeList.add(edge);
    }

    /**
     *Add compEdges to the compEdgeList.
     * @param edges
     */
    public void addEdges(List<CompEdge> edges) {
        compEdgeList.addAll(edges);
    }


}
