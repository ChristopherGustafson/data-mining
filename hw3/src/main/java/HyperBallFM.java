import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.Map;

public class HyperBallFM {

    Map<Integer, FlajoletMartin> c;

    public HyperBallFM(int n, Graph<Integer, DefaultEdge> graph)  {
        // Initialize a FlajoletMartin counter for every node
        c = new HashMap<>();
        calculateBg(n, graph);
    }

    public void calculateBg(int n, Graph<Integer, DefaultEdge> graph) {
        // Add the node itself to the corresponding FlajoletMartin counter
        for(Integer node: graph.vertexSet()) {
            FlajoletMartin counter = new FlajoletMartin();
            counter.add(node);
            c.put(node, counter);
        }

        boolean didUpdate = true;
        int t = 0;
        while(didUpdate) {
            System.out.println("Radius " + t);
            didUpdate = false;
            FlajoletMartin a;
            for (Integer v: graph.vertexSet()) {
                a = c.get(v);
                for (DefaultEdge edge : graph.edgesOf(v)) {
                    a = union(c.get(graph.getEdgeTarget(edge)), a);
                }
                // Do something with a and c[i]
                //System.out.printf("BG(%d, %d) = %d%n", v, t+1, a.getDistinct());
                if (c.get(v).getDistinct() != a.getDistinct()) {
                    didUpdate = true;
                }
                c.put(v, a);
            }
            t++;
        }
    }

    // Union of two Flajolet-Martin Counters is just the max
    private FlajoletMartin union(FlajoletMartin a, FlajoletMartin b){
        return a.getDistinct() > b.getDistinct() ? a : b;
    }


}
