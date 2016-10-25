package LEX2;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private static int ID = 0;

    public Node() {
        this.id = ID++;
    }

    public int getId() {
        return id;
    }

    public static void reset() {
        ID = 0;
    }

    @Override
    public String toString() {
        return id + "";
    }

}
