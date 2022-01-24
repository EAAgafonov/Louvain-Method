package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class GraphMain {
    private final ArrayList<Node> nodes;

    public GraphMain() {
        this.nodes = new ArrayList<>();
        GraphLoader.loadGraph(this, "data/data3.txt");
    }

    public Node addVertex(int n) {
        Node n1 = new Node(n);

        if (!nodes.contains(n1)) {
            nodes.add(n1);
        } else {
            for (Node node : nodes) {
                if (node.equals(n1)) {
                    return node;
                }
            }
        }
        return n1;
    }

    public void addEdge(int from, int to) {
        Node n1 = addVertex(from);
        Node n2 = addVertex(to);

        n1.addToEdgesList(n2);
        n2.addToEdgesList(n1);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    //sets different communities for each node
    private void setCommunitiesBeforeLouvain() {
        int community = 0;
        for (Node node : nodes) {
            node.setCommunity(++community);
        }
    }

    //returns Qscore for two nodes
    private double scoreOfTwoNodes(Node node1, Node node2) {
        double numEdges = numEdges();
        return (1d / numEdges) * (weightBetweenNodes(node1, node2) - ((node1.getNumEdges() * node2.getNumEdges()) / numEdges));
    }

    //returns total num of edges in a graph
    private double numEdges() {
        double count = 0;
        for (Node node : nodes)
            count += node.getNumEdges();
        return count;
    }

    private int weightBetweenNodes(Node node1, Node node2) {
        int weight = 0;
        for (Node node : nodes) {
            if (node.equals(node1)) {
                for (Edge edge : node.getEdgesTo()) {
                    if (edge.getNodeTo().equals(node2)) {
                        weight += edge.getWeight();
                    }
                }
            }
        }
        return weight;
    }

    //first step - modularity optimization
    public void louvainMethodFirstStep(ArrayList<Node> graph) {
        setCommunitiesBeforeLouvain();
        int condition = 0;

        while (condition < graph.size()) {
            condition = 0;

            for (Node node : graph) {
                HashMap<Integer, Double> communityScores = new HashMap<>();
                for (Edge edge : node.getEdgesTo()) {
                    Double qScore = scoreOfTwoNodes(node, edge.getNodeTo());
                    if (!communityScores.containsKey(edge.getNodeTo().getCommunity())) {
                        communityScores.put(edge.getNodeTo().getCommunity(), qScore);
                    } else {
                        communityScores.put(edge.getNodeTo().getCommunity(), qScore + communityScores.get(edge.getNodeTo().getCommunity()));
                    }
                }
                Integer key = Collections.max(communityScores.entrySet(), HashMap.Entry.comparingByValue()).getKey();

                if (node.getCommunity() != key)
                    node.setCommunity(key);
                else
                    condition++;
            }
        }
    }

    //second step - community aggregation
    public ArrayList<Node> louvainMethodSecondStep(ArrayList<Node> oldGraph) {
        ArrayList<Node> graphNew = addNewGraphNodes(oldGraph);

        graphNew = addEdgesInAndOutCommunities(oldGraph, graphNew);

        return graphNew;
    }

    private Edge returnReverseEdge(Edge edgeToFlip) {
        Edge resultEdge = null;

        for (Node node : nodes) {
            if (node.equals(edgeToFlip.getNodeTo())) {
                for (Edge edge : node.getEdgesTo()) {
                    if (edge.getNodeTo().equals(edgeToFlip.getNodeFrom())) {
                        resultEdge = edge;
                    }
                }
            }
        }
        return resultEdge;
    }

    private ArrayList<Node> addNewGraphNodes(ArrayList<Node> oldGraph) {
        ArrayList<Node> graphNew = new ArrayList<>();
        ArrayList<Integer> communitiesNum = new ArrayList<>();
        for (Node node : oldGraph) {
            if (!communitiesNum.contains(node.getCommunity())) {
                Node nodeNew = new Node(node.getCommunity());
                graphNew.add(nodeNew);
                communitiesNum.add(node.getCommunity());
            }
        }
        return graphNew;
    }

    //метод считающий ребра внутри и вне сообществ
    private ArrayList<Node> addEdgesInAndOutCommunities(ArrayList<Node> oldGraph, ArrayList<Node> graphNew) {
        //community -> sum num of edges within
        HashMap<Integer, Integer> edgesWithinCommunities = new HashMap<>();

        //community -> sum num of edges outside
        HashMap<Edge, Integer> edgesBetweenCommunities = new HashMap<>();

        ArrayList<Edge> checkedEdges = new ArrayList<>();
        ArrayList<Integer> communitiesWithin = new ArrayList<>();
        ArrayList<Integer> communitiesBetween = new ArrayList<>();

        for (Node node : oldGraph) {
            for (Edge edge : node.getEdgesTo()) {
                if (!checkedEdges.contains(edge)) {
                    if (node.getCommunity() == edge.getNodeTo().getCommunity()) {
                        //add to edgesWithinCommunities

                        if (!communitiesWithin.contains(node.getCommunity())) {
                            edgesWithinCommunities.put(node.getCommunity(), 1);
                            communitiesWithin.add(node.getCommunity());
                        } else {
                            edgesWithinCommunities.put(node.getCommunity(), edgesWithinCommunities.get(node.getCommunity()) + 1);
                        }

                    } else {
                        //add to edgesBetweenCommunities

                        if (!communitiesBetween.contains(edge.getNodeTo().getCommunity())) {

                            edgesBetweenCommunities.put(edge, 1);
                            communitiesBetween.add(node.getCommunity());
                        } else {
                            edgesBetweenCommunities.put(edge, edgesBetweenCommunities.get(edge) + 1);
                        }

                    }

                    checkedEdges.add(edge);
                    checkedEdges.add(returnReverseEdge(edge));
                }
            }
        }

        GraphLoader.loadNewGraph(edgesWithinCommunities, edgesBetweenCommunities, graphNew);

        return graphNew;
    }


}
