import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String dataFilePath = "data/web-NotreDame.txt";
        String nodeDelimiter = "\t";
        int commentLines = 4;
        int n = 1005;

        // Initialize graph with all nodes/vertexes
        Graph<Integer, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for(int i = 0; i < n; i++) {
            graph.addVertex(i);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFilePath));
            // Skip comments in file
            for(int i = 0; i < commentLines; i++){
                br.readLine();
            }

            // Read input file, add edges to graph data structure
            String line;
            while((line = br.readLine()) != null) {
                String[] edgeNodes = line.split(nodeDelimiter);
                // Add source node to graph if not in it already
                int sourceNode = Integer.parseInt(edgeNodes[0]);
                if(!graph.containsVertex(sourceNode)) graph.addVertex(sourceNode);

                // Add target node to graph if not in it already
                int targetNode = Integer.parseInt(edgeNodes[1]);
                if(!graph.containsVertex(targetNode)) graph.addVertex(targetNode);

                // Add edge between source node and target node to graph
                graph.addEdge(sourceNode, targetNode);
            }

            // Create new hyperloglog counter and add all nodes
            HyperLogLog hll = new HyperLogLog();
            hll.add(graph.vertexSet());
            // print the approximated distinct nodes
            System.out.printf("Approximated number of distinct nodes: %f\n", hll.size());


            int runs = 5;
            double totalExecTime = 0;
            for(int i = 0; i < runs; i++) {
                System.out.printf("Run %d of HyperBal ***********\n", i+1);
                long startTime = System.nanoTime();
                // Calculate the HyperBall ball radiuses
                new HyperBall(n, graph);
                long endTime = System.nanoTime();
                totalExecTime += (endTime-startTime)/1000000;
            }
            System.out.printf("Avg exec time after %d runs: %f ms", runs, (totalExecTime/runs));
        }
        catch(Exception e) {
            e.printStackTrace();
        }


    }
}
