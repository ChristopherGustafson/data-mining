import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HyperBall {
    Map<Integer, HyperLogLog> c;

    public HyperBall(int n, Graph<Integer, DefaultEdge> graph)  {
        // Calculate all
        c = new HashMap<>();
        calculateBg(n, graph);
    }

    public void calculateBg(int n, Graph<Integer, DefaultEdge> graph) {
        // Add the node itself to the corresponding FlajoletMartin counter
        for(Integer node: graph.vertexSet()) {
            HyperLogLog counter = new HyperLogLog();
            counter.add(node);
            c.put(node, counter);
        }

        boolean didUpdate = true;
        int t = 1;
        while(didUpdate) {
            System.out.println("Radius " + t);
            // Create a new map for storing new counters
            Map<Integer, HyperLogLog> newCounters = new HashMap<>();

            didUpdate = false;
            HyperLogLog a;
            // For every node in the graph
            for (Integer v: graph.vertexSet()) {
                a = c.get(v);
                // Calculate the union of its previous ball radius and the adjacent edges
                for (DefaultEdge edge : graph.edgesOf(v)) {
                    a = union(c.get(graph.getEdgeTarget(edge)), a);
                }
                // Save v, a to disk
                newCounters.put(v, a);
                // Do something with a and c[i]
                System.out.printf("BG(%d, %d) = %f\n", v, t, a.size());
                if (c.get(v).size() != a.size()) {
                    didUpdate = true;
                }
            }
            // Update c with the new counters
            for(Map.Entry<Integer, HyperLogLog> entry: newCounters.entrySet()) {
                c.put(entry.getKey(), entry.getValue());
            }
            t++;
        }
    }

    // Union of two HyperLogLog Counters is the max of each individual entry
    private HyperLogLog union(HyperLogLog a, HyperLogLog b){
        long[] retM = new long[a.getM().length];
        for(int i = 0; i < retM.length; i++) {
            retM[i] = Math.max(a.getM()[i], b.getM()[i]);
        }
        return new HyperLogLog(retM);
    }


}
