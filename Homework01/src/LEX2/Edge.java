package LEX2;

public class Edge {
    private Node begin;
    private Node end;
    private String label;

    public Edge() {
        super();
    }

    public Edge(Node begin, Node end) {
        super();
        this.begin = begin;
        this.end = end;
    }

    public Edge(Node begin, Node end, String label) {
        super();
        this.begin = begin;
        this.end = end;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Edge getEdge() {
        return this;
    }

    public void setEdge(Edge edge) {
        this.begin = edge.getBegin();
        this.end = edge.getEnd();
        this.label = edge.getLabel();
    }

    public Node getBegin() {
        return begin;
    }

    public void setBegin(Node begin) {
        this.begin = begin;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Edge [begin=" + begin + ", end=" + end + ", label=" + label
                + "]";
    }

}
