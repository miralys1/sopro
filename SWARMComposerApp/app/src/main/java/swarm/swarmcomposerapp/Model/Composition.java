package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Model representation of a composition.
 */
public class Composition {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("owner")
    private SimpleUser owner;
    private long lastUpdate;
    private long dateCreated; //Implementation not planned
    @SerializedName("nodes")
    List<Node> nodeList = new ArrayList<>();
    @SerializedName("edges")
    List<Edge> edgeList = new ArrayList<>();
    @SerializedName("editable")
    boolean editable;

    /**
     * Creates a not-detailed, simple or zombie Composition.
     * (Depending on your desired naming scheme).
     * Such a composition should only be used for meta data purposes.
     * Ergo: You will initially not be able to draw a Composition View with such a composition.
     *
     * @param id
     * @param name
     * @param owner
     */
    public Composition(long id, String name, SimpleUser owner) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();
        setLastUpdate();
    }

    /**
     * Creates a Composition including sufficient enough details for drawing it
     * on a CompositionView.
     *
     * @param id
     * @param name
     * @param owner
     * @param edgeList
     * @param nodeList
     */
    public Composition(long id, String name, SimpleUser owner, List<Edge> edgeList,
                       List<Node> nodeList) {
        this(id, name, owner);
        this.edgeList = edgeList;
        this.nodeList = nodeList;
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

    public void setLastUpdate() {
        this.lastUpdate = System.currentTimeMillis() / 1000L;
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
     * Add compEdges to the edgeList.
     *
     * @param edges
     */
    public void addEdges(List<Edge> edges) {
        edgeList.addAll(edges);
    }

}
