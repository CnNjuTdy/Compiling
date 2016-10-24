package LEX2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tonndiyee on 2016/10/23.
 */
public class Graph {
    private List<Edge> edges;
    private Node start;
    private Node end;
    private List<Character> alphabet;
    private int maxNode;

    public Graph() {
        this.start = new Node();
        this.end = new Node();
        this.edges = new ArrayList<Edge>();
        this.edges.add(new Edge(start, end, "epsilon"));
        this.alphabet = new ArrayList<Character>();
    }

    public Graph(char c) {
        this.start = new Node();
        this.end = new Node();
        this.edges = new ArrayList<Edge>();
        this.edges.add(new Edge(start, end, c + ""));
        this.alphabet = new ArrayList<Character>();
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

    public List<Character> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<Character> alphabet) {
        this.alphabet = alphabet;
    }

    public void setMaxNode() {
        int maxId = 0;
        for (Edge edge : edges) {
            int tempId = Math.max(edge.getEnd().getId(), edge.getBegin().getId());
            maxId = Math.max(maxId, tempId);
        }

        this.maxNode = maxId;
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

        Edge edge1 = new Edge(endNode, startNode, "epsilon");
        Edge edge2 = new Edge(startNode, graph.getStart(), "epsilon");
        Edge edge3 = new Edge(graph.getEnd(), endNode, "epsilon");

        graph.edges.add(edge1);
        graph.edges.add(edge2);
        graph.edges.add(edge3);
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
            printString += edges.get(i) + "\n";
        }

        printString+="alphabet:"+" ";

        for (int i = 0; i < alphabet.size(); i++) {
            if (i == alphabet.size() - 1) {
                printString += alphabet.get(i);
            } else {
                printString += alphabet.get(i) + ",";
            }
        }

        printString = printString +"\n"+"maxNode: "+maxNode;
        return printString;
    }
}

