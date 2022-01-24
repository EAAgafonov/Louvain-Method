package graph;

import java.util.Date;
import java.util.Objects;

public class Edge {
    private int weight;
    //    private double modularityScore;
    private final Node nodeFrom;
    private final Node nodeTo;
    private final long createTime;

    public Edge(Node nodeFrom, Node nodeTo) {
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.weight = 1;
        this.createTime = new Date().getTime();
    }

    public Edge(Node nodeFrom, Node nodeTo, int weight) {
        this.weight = weight;
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.createTime = new Date().getTime();
    }

    public Edge(Edge edgeOld) {
        this.weight = edgeOld.weight;
        this.nodeFrom = edgeOld.getNodeFrom();
        this.nodeTo = edgeOld.getNodeTo();
        this.createTime = new Date().getTime();
    }

    public Node getNodeTo() {
        return nodeTo;
    }

    public Node getNodeFrom() {
        return nodeFrom;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return createTime == edge.createTime && nodeFrom.equals(edge.nodeFrom) && nodeTo.equals(edge.nodeTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeFrom, nodeTo, createTime);
    }

    @Override
    public String toString() {
        return nodeTo.getId() + "";
    }
}
