package graph;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GraphLoader {
    public static void loadGraph(GraphMain g, String filename) {
        Scanner sc;

        try {
            sc = new Scanner(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (sc.hasNext()) {
            int id1 = sc.nextInt();
            int id2 = sc.nextInt();
            g.addEdge(id1, id2);
        }
        sc.close();
        System.out.println("Graph loaded.");
    }

    //community aggregation of Louvain algorithm
    public static void loadNewGraph(HashMap<Integer, Integer> edgesWithinCommunities, HashMap<Edge, Integer> edgesBetweenCommunities, ArrayList<Node> graphNew) {
        edgesWithinCommunities.forEach((k, v) -> {
            for (Node node : graphNew) {
                if (node.getId() == k) {
                    node.addToEdgesListWithWeight(node, (v * 2));
                }
            }
        });

        edgesBetweenCommunities.forEach((edge, weight) -> {
            graphNew.stream().filter(node -> node.getId() == edge.getNodeFrom().getCommunity()).forEach(node -> {
                Node nn = new Node(edge.getNodeTo().getCommunity());
                node.addToEdgesListWithWeight(nn, weight);
            });

        });
    }
}
