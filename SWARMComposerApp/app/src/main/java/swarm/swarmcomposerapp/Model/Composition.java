package swarm.swarmcomposerapp.Model;

import java.util.List;

public class Composition {

    //private long authorID;
    private long id;
    private String owner;
    private long lastChange;
    List<Node> nodeList;
    List<Edge> edgeList;
    boolean editable;


    /**
     * Add one compNode to the CompNodeList
     *
     * @param comp
     */
    public void addComp(Node comp) {
        nodeList.add(comp);
    }

    /**
     * Add compNodes to the nodeList
     *
     * @param comps
     */
    public void addComps(List<Node> comps) {
        nodeList.addAll(comps);
    }


    /**
     * Add one compEdge to the edgeList.
     *
     * @param edge
     */
    public void addEdge(Edge edge) {
        edgeList.add(edge);
    }

    /**
     *Add compEdges to the edgeList.
     * @param edges
     */
    public void addEdges(List<Edge> edges) {
        edgeList.addAll(edges);
    }


}
