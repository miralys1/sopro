package swarm.swarmcomposerapp.Model;

import java.util.List;

/**
 * Model representation of a composition.
 */
public class Composition {
    private long id;
    private SimpleUser owner;
    private long lastChange;
    List<Node> nodeList;
    List<Edge> edgeList;
    boolean editable;

    public Composition(long id, SimpleUser owner) {
        this.id = id;
        this.owner = owner;
    }

    public Composition(long id) {
        this.id = id;

    }

    public long getId() {
        return id;
    }

    public long getLastChange() {
        return lastChange;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public boolean isEditable() {
        return editable;
    }

    /**
     * Add one compNode to the CompNodeList
     *
     * @param node
     */
    public void addNode(Node node) {
        nodeList.add(node);
    }

    /**
     * Add compNodes to the nodeList
     *
     * @param nodes
     */
    public void addComps(List<Node> nodes) {
        nodeList.addAll(nodes);
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
