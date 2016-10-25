package LEX2;

import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Tonndiyee on 2016/10/24.
 */
public class Lab {
    private Graph NFA;
    private List<ArrayList<Integer>> nodeList;
    private List<ArrayList<Integer>> nodeListA;
    private List<ArrayList<Integer>> nodeListB;
    private Stack stack;

    public Lab() {
    }

    public Graph REtoDFA(String REString){
        Graph NFA = REtoNFA(REString);
        this.NFA = NFA;
        nodeList = new ArrayList<ArrayList<Integer>>();
        nodeListA = new ArrayList<ArrayList<Integer>>();
        nodeListB = new ArrayList<ArrayList<Integer>>();
        stack = new Stack();


        return NFAtoDFA();
    }

    private Graph NFAtoDFA() {
        ArrayList<Integer> newBegin = (ArrayList<Integer>) epsilonClosure(NFA.getStart().getId());
        stack.push(newBegin);
        while (!stack.isEmpty()) {
            ArrayList<Integer> node = (ArrayList<Integer>) stack.pop();
            nodeList.add(node);
            addNode(node);
        }


        if (nodeList.size() != nodeListA.size() || nodeList.size() != nodeListB.size()) {
            System.out.println("出现错误");
            System.exit(0);
        }


        Node.reset();
        Graph DFA = new Graph();
        int size = nodeList.size();
        int endId = NFA.getEnd().getId();
        List<Node> nodes = new ArrayList<Node>();
        List<Edge> edges = DFA.getEdges();
        for (int i = 0; i < size; i++) {
            Node node = new Node();
            if (i == 0) {
                DFA.setStart(node);
            }
            if (nodeList.get(i).contains(endId)) {
                DFA.setEnd(node);
            }
            nodes.add(node);
        }
        for (int i = 0; i < size; i++) {
            int end1 = listIndexOf(nodeList,nodeListA.get(i));
            int end2 = listIndexOf(nodeList,nodeListB.get(i));

            Edge edge1 = new Edge(nodes.get(i),nodes.get(end1),"a");
            Edge edge2 = new Edge(nodes.get(i),nodes.get(end2),"b");

            edges.add(edge1);
            edges.add(edge2);
        }
        return DFA;

    }

    private Graph REtoNFA(String REString) {
        Stack NFAStack = new Stack();
        NFAStack.push('#');
        Stack operatorStack = new Stack();
        char REChars[] = REString.toCharArray();

        List<Character> alphabet = new ArrayList<Character>();

        for (int i = 0; i < REChars.length; i++) {
            char theChar = REChars[i];
            if (theChar == '(') {
                operatorStack.push('(');
            } else if (theChar == ')') {
                char operator = (char) operatorStack.pop();
                if (operator == '|') {
                    Graph other = (Graph) NFAStack.pop();
                    Graph graph = (Graph) NFAStack.pop();
                    graph.union(other);
                    NFAStack.push(graph);
                    operatorStack.pop();
                } else if (operator == '.') {
                    Graph other = (Graph) NFAStack.pop();
                    Graph graph = (Graph) NFAStack.pop();
                    graph.join(other);
                    NFAStack.push(graph);
                    operatorStack.pop();
                }
            } else if (theChar == '*') {
                Graph graph = (Graph) NFAStack.pop();
                graph.closure();
                NFAStack.push(graph);

                if (i != REChars.length - 1) {
                    char nextChar = REChars[i + 1];
                    if ((nextChar <= 'z' && nextChar >= 'a') || nextChar == '(') {
                        operatorStack.push('.');
                    }
                }
            } else if (theChar == '|') {
                char nextOp = (char) operatorStack.pop();
                if (nextOp == '|') {
                    Graph graph = (Graph) NFAStack.pop();
                    Graph other = (Graph) NFAStack.pop();
                    graph.union(other);
                    NFAStack.push(graph);

                    operatorStack.push('|');
                } else if (nextOp == '.') {
                    Graph other = (Graph) NFAStack.pop();
                    Graph graph = (Graph) NFAStack.pop();
                    graph.join(other);
                    NFAStack.push(graph);

                    operatorStack.push('|');
                } else {
                    operatorStack.push(nextOp);
                    operatorStack.push('|');
                }
            } else {
                if (theChar <= 'z' && theChar >= 'a') {
                    if (alphabet.indexOf(theChar) < 0) {
                        alphabet.add(theChar);
                    }

                    Graph graph = new Graph(theChar);
                    NFAStack.push(graph);

                    if (i != REChars.length - 1) {
                        char nextChar = REChars[i + 1];
                        if (nextChar <= 'z' && nextChar >= 'a'||nextChar =='(') {
                            operatorStack.push('.');
                        }
                    }
                } else {
                    System.out.println("输入不符合规定");
                    System.exit(0);
                }
            }
        }
        int size = operatorStack.size();
        for (int i = 0; i < size; i++) {
            char operator = (char) operatorStack.pop();
            if (operator == '.') {
                Graph other = (Graph) NFAStack.pop();
                Graph graph = (Graph) NFAStack.pop();
                graph.join(other);
                NFAStack.push(graph);
            } else if (operator == '|') {
                Graph other = (Graph) NFAStack.pop();
                Graph graph = (Graph) NFAStack.pop();
                graph.union(other);
                NFAStack.push(graph);
            } else if (operator == '(') {
                System.out.println("括号不匹配错误");
                System.exit(0);
            } else {
                operatorStack.push(operator);
            }
        }

        Graph NFA = (Graph) NFAStack.pop();
        return NFA;
    }

    //循环调用只计算一次的方法直到前后两次长度相同，获得整个epsilon闭包，算法效率很有瑕疵不过懒得改
    private List<Integer> epsilonClosure(int nodeId) {
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
    private List<Integer> epsilonClosure(List<Integer> nodeIdList) {
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
    private List<Integer> move(List<Integer> nodeIdList, char c) {
        List<Integer> result = new ArrayList<Integer>();
        List<Edge> edges = NFA.getEdges();
        for (int x : nodeIdList) {
            for (Edge edge : edges) {
                if (x == edge.getBegin().getId() && (c + "").equals(edge.getLabel())) {
                    int y = edge.getEnd().getId();
                    if (result.indexOf(y) < 0) {
                        result.add(y);
                    }
                }
            }
        }
        return epsilonClosure(result);
    }

    //向表的第二项和第三项中添加元素
    private void addNode(ArrayList<Integer> node) {
        ArrayList<Integer> nodeA = (ArrayList<Integer>) move(node, 'a');
        ArrayList<Integer> nodeB = (ArrayList<Integer>) move(node, 'b');
        nodeListA.add(nodeA);
        nodeListB.add(nodeB);
        if (!listCotains(nodeList, nodeA)) {
            if (!stackCotains(stack, nodeA)) {
                stack.push(nodeA);
            }
        }
        if (!listCotains(nodeList, nodeB)) {
            if (!stackCotains(stack, nodeB)) {
                stack.push(nodeB);
            }
        }
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

    //数组的栈是否包含一个数组
    private boolean stackCotains(Stack father, ArrayList<Integer> son) {
        int size = father.size();
        for (int i = 0; i < size; i++) {
            ArrayList<Integer> a = (ArrayList<Integer>) father.get(i);
            if (listEqual(a, son)) {
                return true;
            }
        }
        return false;
    }

    //数组的数组是否包含一个数组
    private boolean listCotains(List<ArrayList<Integer>> father, ArrayList<Integer> son) {
        for (ArrayList<Integer> a : father) {
            if (listEqual(a, son)) {
                return true;
            }
        }
        return false;
    }

    //数组的数组中一个数组的下标，不存在返回-1
    private int listIndexOf(List<ArrayList<Integer>> father, ArrayList<Integer> son) {
        for (int i = 0; i < father.size(); i++) {
            if(listEqual(father.get(i),son)){
                return i;
            }
        }
        return -1;
    }

    //两个数组是不是元素都相同
    private boolean listEqual(List<Integer> a, List<Integer> b) {
        for (int x : a) {
            if (b.indexOf(x) < 0) {
                return false;
            }
        }
        for (int y : b) {
            if (a.indexOf(y) < 0) {
                return false;
            }
        }
        return true;
    }

}
