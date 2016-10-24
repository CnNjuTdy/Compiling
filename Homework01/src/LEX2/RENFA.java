package LEX2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Tonndiyee on 2016/10/23.
 */
public class RENFA {


    public Graph analyze(String REString) {
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
                        if (nextChar <= 'z' && nextChar >= 'a') {
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
        NFA.setAlphabet(alphabet);
        NFA.setMaxNode();
        return NFA;
    }


    public static void main(String[] args) {
        RENFA renfa = new RENFA();
//        System.out.println(renfa.analyze("(a|b)*").toString());
        NFADFA nfadfa  = new NFADFA(renfa.analyze("(a|b)*"));
        List<Integer> list = nfadfa.epsilonClosure(6);
        for (int i:list){
            System.out.print(i);
        }
    }

}
