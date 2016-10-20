package CAnalyze;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OutputService {

    List<String> outputStack;
    boolean isError;

    public OutputService() {
        outputStack=new ArrayList<String>();
    }

    public void outputIntoBuffer(String string){
        outputStack.add(string);
    }

    public void output(){
        try {
            FileWriter fw =  new FileWriter("output.txt");
            for(String s: outputStack){
                fw.write(s);
                fw.write(10);
            }
            fw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        if(isError){
            System.out.println("输入有错误");
            return;
        }
        outputStack.forEach(System.out::println);
    }

    public void error(){
        isError=true;
    }

}
