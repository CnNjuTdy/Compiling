package CAnalyze;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class InputService {

    private static final String INPUT_FILE="input.txt";
    public static final char EOF=(char)0;
    Reader reader = null;

    public InputService() {
        File file = new File(INPUT_FILE);
        try {
            reader = new InputStreamReader(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public char getChar() {
        int tempchar;
        try {
            if ((tempchar = reader.read()) != -1) {
                return (char)tempchar;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finishRead();
        return EOF;

    }

    public  void finishRead(){
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

