package LEX2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tonndiyee on 2016/10/24.
 */
public class NFADFA {
    private Graph NFA;

    public NFADFA(Graph NFA) {
        this.NFA = NFA;
    }


    //循环调用只计算一次的方法直到前后两次长度相同，获得整个epsilon闭包
    public List<Integer> epsilonClosure(int nodeId) {
        List<Integer> result = epsilonClosureSimple(nodeId);
        while (true) {
            List<Integer> extra = new ArrayList<Integer>();
            for (int x : result) {
                List<Integer> temp = epsilonClosureSimple(x);
                for (int u : temp) {
                    if (result.indexOf(u) < 0) {
                        extra.add(u);
                    }
                }
                if (extra.size()==0){
                    return result;
                }
            }
        }
    }

    public List<Integer> epsilonClosure(List<Integer> nodeIdList) {
        return null;
    }

    public Graph analyze() {

        return null;
    }

    //仅仅计算一次的epsilon闭包
    private List<Integer> epsilonClosureSimple(int nodeId) {
        List<Integer> result = new ArrayList<Integer>();
        result.add(nodeId);
        List<Edge> edges = NFA.getEdges();
        for (Edge edge : edges) {
            int id = edge.getBegin().getId();
            String label = edge.getLabel();
            if (id == nodeId && label.equals("epsilon")) {
                result.add(edge.getEnd().getId());
            }
        }
        return result;
    }
}
