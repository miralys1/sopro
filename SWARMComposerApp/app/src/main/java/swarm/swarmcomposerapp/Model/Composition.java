package swarm.swarmcomposerapp.Model;

import java.util.List;


/**
 * Model representation of a composition.
 */
public class Composition{
    private long id;
    private String name;
    private SimpleUser owner;
    private long lastUpdate;
    private long dateCreated; //Implementation not planned
    List<Node> nodeList;
    List<Edge> edgeList;
    boolean editable;

    public Composition(long id, String name, SimpleUser owner) {
        this.id = id;
        this.owner = owner;
        this.lastUpdate = System.currentTimeMillis() / 1000L;
    }

    public SimpleUser getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public long getLastUpdate() {
        return lastUpdate;
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
