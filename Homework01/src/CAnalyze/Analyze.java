package CAnalyze;

import java.util.HashMap;

public class Analyze {

    enum STATE {
        DONE,//正常情况
        INVAR,//标识符
        INADD, INMINUS,//加减符号
        INLESS, INMORE, INEQUAL,//小于大于等于
        INEXCLAMATORY,//惊叹号
        INAND, INOR,//且 或
        INSOLIDUS,//斜线
        INDIGIT,//数字
        INDECIMALS,//小数
        ANNOTATION_ONE_LINE, ANNOTATION_MULTI_LINE, ANNOTATION_MULTI_LINE_ASTERISK,//各种注释
        SINGLE_QUOTE_MARK, DOUBLE_QUOTE_MARK//单引号双引号
    }

    enum TOKEN {
        KEYWORDS,//关键字
        IDENTIFIER,//标识符
        OPERATOR,//操作符
        DELIMITER,//分隔符
        INT,//整数
        DOUBLE,//浮点数
        ANNOTATION//注释符
    }

    static HashMap<TOKEN, String> map = new HashMap<TOKEN, String>();

    static String KEYWORDS[] = {"break", "case", "char", "const",
            "continue", "default", "do", "double", "else", "enum", "float", "for", "goto", "if",
            "long", "main", "short", "signed", "sizoef", "static", "switch", "unsigned", "void"};
    InputService inputService;
    OutputService outputService;
    STATE state;
    StringBuffer charBuffer;
    StringBuffer digitBuffer;
    StringBuffer annotationBuffer;
    StringBuffer quoteMarkBuffer;

    public static void main(String[] args) {
        try {
            initMap();
            new Analyze().AnalyzeStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initMap() {
        map.put(TOKEN.KEYWORDS, "关键字");
        map.put(TOKEN.IDENTIFIER, "标识符");
        map.put(TOKEN.OPERATOR, "操作符");
        map.put(TOKEN.DELIMITER, "分隔符");
        map.put(TOKEN.INT, "整数");
        map.put(TOKEN.DOUBLE, "浮点数");
        map.put(TOKEN.ANNOTATION, "注释符");
    }

    public void AnalyzeStart() throws Exception {

        inputService = new InputService();
        outputService = new OutputService();
        state = STATE.DONE;
        charBuffer = new StringBuffer();
        digitBuffer = new StringBuffer();
        annotationBuffer = new StringBuffer();
        quoteMarkBuffer = new StringBuffer();

        while (true) {
            //读取一个字符
            char nextChar = inputService.getChar();
            if (nextChar == InputService.EOF) {
                //说明已经读完程序
                break;
            }
            switch (state) {
                case DONE:
                    readNewWord(nextChar);
                    break;
                case INVAR:
                    if (Character.isLetterOrDigit(nextChar)) {
                        charBuffer.append(nextChar);
                    } else {
                        if (isReserved(charBuffer.toString())) {
                            outputService.outputIntoBuffer(map.get(TOKEN.KEYWORDS) + "		" + charBuffer);
                        } else {
                            outputService.outputIntoBuffer("标识符		" + charBuffer);
                        }
                        state = STATE.DONE;
                        //清空buffer
                        charBuffer = new StringBuffer();
                        ;
                        readNewWord(nextChar);
                    }
                    break;
                case INADD:
                    if (nextChar == '+' || nextChar == '=') {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		+" + nextChar);
                        state = STATE.DONE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		+");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INLESS:
                    if (nextChar == '<' || nextChar == '=') {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		<" + nextChar);
                        state = STATE.DONE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		<");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INMORE:
                    if (nextChar == '>' || nextChar == '=') {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		>" + nextChar);
                        state = STATE.DONE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		>");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INEQUAL:
                    if (nextChar == '=') {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		=" + nextChar);
                        state = STATE.DONE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		=");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INMINUS:
                    if (nextChar == '-' || nextChar == '=') {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		-" + nextChar);
                        state = STATE.DONE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		-");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INEXCLAMATORY:
                    if (nextChar == '=') {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		!" + nextChar);
                        state = STATE.DONE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		!");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INAND:
                    if (nextChar == '&') {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		&" + nextChar);
                        state = STATE.DONE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		&");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INOR:
                    if (nextChar == '|') {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		|" + nextChar);
                        state = STATE.DONE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		|");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INSOLIDUS:
                    if (nextChar == '/') {
                        outputService.outputIntoBuffer(map.get(TOKEN.ANNOTATION) + "		//");
                        state = STATE.ANNOTATION_ONE_LINE;
                    } else if (nextChar == '*') {
                        outputService.outputIntoBuffer(map.get(TOKEN.ANNOTATION) + "		/*");
                        state = STATE.ANNOTATION_MULTI_LINE;
                    } else {
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		/");
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    }
                    break;
                case INDIGIT:
                    if (Character.isDigit(nextChar)) {
                        digitBuffer.append(nextChar);
                    } else if (nextChar == '.') {
                        digitBuffer.append(nextChar);
                        state = STATE.INDECIMALS;
                    } else if (Character.isLetter(nextChar)) {
                        outputService.error();
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    } else {
                        state = STATE.DONE;
                        outputService.outputIntoBuffer(map.get(TOKEN.INT) + "		" + digitBuffer);
                        //清空buffer
                        digitBuffer = new StringBuffer();
                        readNewWord(nextChar);
                    }
                    break;
                case INDECIMALS:
                    if (Character.isDigit(nextChar)) {
                        digitBuffer.append(nextChar);
                    } else if (Character.isLetter(nextChar)) {
                        outputService.error();
                        state = STATE.DONE;
                        readNewWord(nextChar);
                    } else {
                        state = STATE.DONE;
                        outputService.outputIntoBuffer(map.get(TOKEN.DOUBLE) + "		" + digitBuffer);
                        //清空buffer
                        digitBuffer = new StringBuffer();
                        readNewWord(nextChar);
                    }
                    break;
                case ANNOTATION_ONE_LINE:
                    if (nextChar == '\n') {
                        //跳出单行注释
                        state = STATE.DONE;
                        outputService.outputIntoBuffer("注释内容		" + annotationBuffer);
                        annotationBuffer = new StringBuffer();
                    } else {
                        annotationBuffer.append(nextChar);
                    }
                    break;
                case ANNOTATION_MULTI_LINE:
                    if (nextChar == '*') {
                        state = STATE.ANNOTATION_MULTI_LINE_ASTERISK;
                    }
                    annotationBuffer.append(nextChar);
                    break;
                case ANNOTATION_MULTI_LINE_ASTERISK:
                    if (nextChar == '/') {
                        //跳出多行注释
                        state = STATE.DONE;
                        outputService.outputIntoBuffer("注释内容		" + annotationBuffer.substring(0, annotationBuffer.length() - 2));
                        outputService.outputIntoBuffer(map.get(TOKEN.ANNOTATION) + "		*/");
                        annotationBuffer = new StringBuffer();
                    } else if (nextChar == '*') {
                        annotationBuffer.append(nextChar);
                    } else {
                        state = STATE.ANNOTATION_MULTI_LINE;
                        annotationBuffer.append(nextChar);
                    }
                    break;
                case SINGLE_QUOTE_MARK:
                    if (nextChar == '\'') {
                        //单引号结束
                        state = STATE.DONE;
                        outputService.outputIntoBuffer("字符		" + quoteMarkBuffer);
                        outputService.outputIntoBuffer("单引号		'");
                        quoteMarkBuffer = new StringBuffer();
                    } else {
                        quoteMarkBuffer.append(nextChar);
                    }
                    break;
                case DOUBLE_QUOTE_MARK:
                    if (nextChar == '"') {
                        //双引号结束
                        state = STATE.DONE;
                        outputService.outputIntoBuffer("字符串		" + quoteMarkBuffer);
                        outputService.outputIntoBuffer("双引号		\"");
                        quoteMarkBuffer = new StringBuffer();
                    } else {
                        quoteMarkBuffer.append(nextChar);
                    }
                    break;
            }


        }
        outputService.output();

    }


    private void readNewWord(char nextChar) {
        if (Character.isLetter(nextChar)) {
            charBuffer.append(nextChar);
            state = STATE.INVAR;
        } else if (Character.isDigit(nextChar)) {
            digitBuffer.append(nextChar);
            state = STATE.INDIGIT;
        } else {
            switch (nextChar) {
                case '+':
                    state = STATE.INADD;
                    break;
                case '-':
                    state = STATE.INMINUS;
                    break;
                case '<':
                    state = STATE.INLESS;
                    break;
                case '>':
                    state = STATE.INMORE;
                    break;
                case '=':
                    state = STATE.INEQUAL;
                    break;
                case '!':
                    state = STATE.INEXCLAMATORY;
                    break;
                case '&':
                    state = STATE.INAND;
                    break;
                case '|':
                    state = STATE.INOR;
                    break;
                case '/':
                    state = STATE.INSOLIDUS;
                    break;
                case '\'':
                    state = STATE.SINGLE_QUOTE_MARK;
                    outputService.outputIntoBuffer("单引号		" + nextChar);
                    break;
                case '"':
                    state = STATE.DOUBLE_QUOTE_MARK;
                    outputService.outputIntoBuffer("双引号		" + nextChar);
                    break;
                case ';':
                    outputService.outputIntoBuffer("分号		" + nextChar);
                    break;
                case ':':
                    outputService.outputIntoBuffer("冒号		" + nextChar);
                    break;
                case '{':
                    outputService.outputIntoBuffer("左大括号		" + nextChar);
                    break;
                case '}':
                    outputService.outputIntoBuffer("右大括号		" + nextChar);
                    break;
                case ',':
                    outputService.outputIntoBuffer("逗号		" + nextChar);
                    break;
                case '.':
                    outputService.outputIntoBuffer("句号		" + nextChar);
                    break;
                default:
                    if (nextChar == '(' || nextChar == ')' || nextChar == '[' || nextChar == ']' || nextChar == '?' ||
                            nextChar == '~' || nextChar == '*' || nextChar == '^' || nextChar == '%') {
                        //这些为只占一个字节就能确定的操作符
                        outputService.outputIntoBuffer(map.get(TOKEN.OPERATOR) + "		" + nextChar);
                    } else if (nextChar == ' ' || nextChar == '\t' || nextChar == '\n' || nextChar == '\r') {
                        //空格换行不算

                    } else {
                        outputService.outputIntoBuffer("不能识别的字符" + (int) nextChar);
                    }

                    break;
            }
        }
    }

    /**
     * 判断保留字
     *
     * @return
     */
    private boolean isReserved(String string) {
        for (String s : KEYWORDS) {
            if (string.equals(s)) {
                return true;
            }
        }
        return false;
    }


}

