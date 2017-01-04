import java.util.ArrayList;
import java.util.List;

public class Vertex {

    String id;
    private List<Edge> edges = new ArrayList<>();

    Vertex(String id) {
        this.id = id;
        this.edges = new ArrayList<>();
    }

    public List<Vertex> getNeighbors() {
        List<Vertex> neighbors = new ArrayList<>();
        for (Edge edge : this.edges) {
            neighbors.add(edge.destination);
        }
        return neighbors;
    }

    void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    Integer getDistance(Vertex neighbor) {
        for (Edge e : this.edges) {
            if (e.source == this && e.destination == neighbor) {
                return e.length;
            }
        }
        return 0;
    }
}

