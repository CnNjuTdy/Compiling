package LEX2;

import java.util.Stack;
/**
 * Created by Tonndiyee on 2016/10/23.
 */
public class RENFA {
    enum OPERATOR {
        JOIN,//链接符号(书写时一般省略)
        OR,//选择符号|
        LEFT_BRACKET//左括号(
    }
    private String REString;
    private Stack<OPERATOR> operatorStack;
    private Stack<Graph> NFAStack;
    public RENFA(String s){
        REString = s;
        operatorStack = new Stack<>();
        NFAStack = new Stack<>();
    }

    public void Analyze(){
        char REChars[] = REString.toCharArray();
        for(int i=0;i<REChars.length;i++){
            char theChar = REChars[i];
            if(theChar == '('){
                operatorStack.push(OPERATOR.LEFT_BRACKET);
            }
            else if(theChar == ')'){
                char operator = operatorStack.pop();
            }
            else if(theChar == '*'){

            }
            else if(theChar == '|'){

            }
            else {

            }
        }
    }

    public Graph getNFA() {
        return NFAStack.pop();
    }
}
