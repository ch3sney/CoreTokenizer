import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class CoreScanner {
    private ArrayList<Integer> tokens;
    public CoreScanner(String file) throws IOException {
        // Instantiate buffered reader
        BufferedReader reader = new BufferedReader(new FileReader(file));

        // poop
        int returnedCode= 1;
        while(returnedCode!= -1){
            returnedCode = tokenizeLine(reader);
        }


    }

    private int tokenizeLine(BufferedReader reader) throws IOException {
        String currLine = reader.readLine();
        while(currLine.isEmpty()){
            if()
        }
    }

    public int getToken(){
        return 0;
    }

    public int intVal(){
        return 0;
    }

    public String idName(){
        return "";
    }

}