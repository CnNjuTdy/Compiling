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


    //循环调用只计算一次的方法直到前后两次长度相同，获得整个epsilon闭包，算法效率很有瑕疵不过懒得改
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
            }
            if (extra.size() == 0) {
                return result;
            }
            for (int u : extra) {
                result.add(u);
            }
        }
    }


    //挨个调用各个数字的闭包算法再合并，效率很差
    public List<Integer> epsilonClosure(List<Integer> nodeIdList) {
        List<Integer> result = new ArrayList<Integer>();
        for (int x : nodeIdList) {
            List<Integer> temp = epsilonClosure(x);
            for (int y : temp) {
                if (result.indexOf(y) < 0) {
                    result.add(y);
                }
            }
        }
        return result;

    }


    //已经计算了move(A,a)的epsilon闭包，我觉得这个算法要爆炸了
    public List<Integer> move(List<Integer> nodeIdList, char c) {
        List<Integer> result = new ArrayList<Integer>();
        List<Edge> edges = NFA.getEdges();
        for (int x : nodeIdList) {
            for (Edge edge : edges) {
                if (x == edge.getBegin().getId() && (c+"").equals(edge.getLabel())) {
                    int y = edge.getEnd().getId();
                    if (result.indexOf(y)<0){
                        result.add(y);
                    }
                }
            }
        }
        return epsilonClosure(result);
    }


    public Graph analyze() {
        int beginNum = NFA.getStart().getId();
        List<Integer> newBegin = epsilonClosure(beginNum);

        List<ArrayList<Integer>> nodeList = new ArrayList<ArrayList<Integer>>();
        List<ArrayList<Integer>> nodeListA = new ArrayList<ArrayList<Integer>>();
        List<ArrayList<Integer>> nodeListB = new ArrayList<ArrayList<Integer>>();

        nodeList.add((ArrayList<Integer>) newBegin);
        nodeListA.add((ArrayList<Integer>)move(newBegin,'a'));
        nodeListA.add((ArrayList<Integer>)move(newBegin,'b'));

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
    private boolean listCotains(List<ArrayList<Integer>> father ,ArrayList<Integer> son){
            for(ArrayList<Integer> a:father){
                if(listEqual(a,son)){
                    return true;
                }
            }
            return false;
    }
    private boolean listEqual(List<Integer> a,List<Integer> b){
        for(int x:a){
            if(b.indexOf(x)<0){
                return false;
            }
        }
        for (int y:b){
            if (a.indexOf(y)<0){
                return false;
            }
        }
        return true;
    }

}
