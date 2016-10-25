package LEX2;

import com.sun.org.apache.bcel.internal.generic.LADD;

/**
 * Created by Tonndiyee on 2016/10/23.
 */
public class Test {
    public static void main(String[] args) {
        Lab lab = new Lab();
        System.out.println(lab.REtoDFA("(a|b)*abb"));
    }


}
