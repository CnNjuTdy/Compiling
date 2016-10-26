package LEX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tonndiyee on 2016/10/23.
 */
public class Graph {
    private List<Edge> edges;
    private Node start;
    private Node end;

    public Graph() {
        this.edges = new ArrayList<Edge>();
    }

    public Graph(char c) {
        this.start = new Node();
        this.end = new Node();
        this.edges = new ArrayList<Edge>();
        this.edges.add(new Edge(start, end, c + ""));
    }

    public Node getEnd() {
        return end;
    }

    public Node getStart() {
        return start;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public void closure() {
        closure(this);
    }

    public void join(Graph other) {
        join(this, other);
    }

    public void union(Graph other) {
        union(this, other);
    }

    private void closure(Graph graph) {
        Node startNode = new Node();
        Node endNode = new Node();

        Edge edge1 = new Edge(startNode, endNode, "epsilon");
        Edge edge2 = new Edge(startNode, graph.getStart(), "epsilon");
        Edge edge3 = new Edge(graph.getEnd(), endNode, "epsilon");
        Edge edge4 = new Edge(graph.getEnd(),graph.getStart(),"epsilon");

        graph.edges.add(edge1);
        graph.edges.add(edge2);
        graph.edges.add(edge3);
        graph.edges.add(edge4);
        graph.start = startNode;
        graph.end = endNode;
    }

    private void join(Graph graph, Graph other) {
        Edge edge = new Edge(graph.getEnd(), other.getStart(), "epsilon");
        for (int i = 0; i < other.getEdges().size(); i++) {
            graph.edges.add(other.getEdges().get(i));
        }
        graph.edges.add(edge);
        graph.end = other.end;
    }

    private void union(Graph graph, Graph other) {
        Node startNode = new Node();
        Node endNode = new Node();
        Edge edge1 = new Edge(startNode, graph.getStart(), "epsilon");
        Edge edge2 = new Edge(startNode, other.getStart(), "epsilon");
        Edge edge3 = new Edge(graph.getEnd(), endNode, "epsilon");
        Edge edge4 = new Edge(other.getEnd(), endNode, "epsilon");

        for (int i = 0; i < other.getEdges().size(); i++) {
            graph.edges.add(other.getEdges().get(i));
        }
        graph.edges.add(edge1);
        graph.edges.add(edge2);
        graph.edges.add(edge3);
        graph.edges.add(edge4);

        graph.start = startNode;
        graph.end = endNode;
    }

    @Override
    public String toString() {
        String printString = "Start=" + this.start + "  End=" + this.end + "\n";
        for (int i = 0; i < edges.size(); i++) {
            if(i!=edges.size()-1) {
                printString += edges.get(i) + "\n";
            }
            else {
                printString += edges.get(i);
            }
        }
        return printString;
    }
}

