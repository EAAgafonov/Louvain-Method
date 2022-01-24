import graph.GraphMain;
import graph.Node;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        GraphMain graph = new GraphMain();

        graph.louvainMethodFirstStep(graph.getNodes());
        System.out.println(graph.getNodes());
        
        System.out.println();

        ArrayList<Node> result = graph.louvainMethodSecondStep(graph.getNodes());
        System.out.println(result);


    }
}
