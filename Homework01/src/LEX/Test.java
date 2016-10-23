package LEX;
public class Test {
    public static void main(String[] args) {
//        try {
//            String regexString4 = "(a|b)*";
//            Regex regex4 = new Regex(regexString4);
//            System.out.println(regex4.getRegex());
//            System.out.println(regex4.transformNFA());
//            regex4.reset();
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        String s ="(";
        char[] x = s.toCharArray();
        for(char c:x){
            System.out.println(c);
        }

    }
}
