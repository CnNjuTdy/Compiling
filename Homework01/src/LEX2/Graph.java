package LEX2;
import java.util.List;

/**
 * Created by Tonndiyee on 2016/10/23.
 */
public class Graph {
    List<Edge> edges;
    private Node start;
    private Node end;


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
}

