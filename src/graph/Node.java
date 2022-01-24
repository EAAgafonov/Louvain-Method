package graph;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Node {
    private final int id;
    private final ArrayList<Edge> edgesTo;
    private int community;
    private final long createTime;

    public Node(int id) {
        this.id = id;
        this.edgesTo = new ArrayList<>();
        this.community = 0;
        this.createTime = new Date().getTime();
    }

    public int getId() {
        return id;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public void addToEdgesList(Node node) {
        edgesTo.add(new Edge(this, node));
    }

    public void addToEdgesListWithWeight(Node node, int weight) {
        edgesTo.add(new Edge(this, node, weight));
    }

    public int getNumEdges() {
        return edgesTo.size();
    }

    public ArrayList<Edge> getEdgesTo() {
        return edgesTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id && createTime == node.createTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createTime);
    }

    @Override
    public String toString() {
        return "\n" + getId() + "->" + edgesTo + "-(" + getCommunity() + ")";
    }


}
