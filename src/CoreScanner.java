import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CoreScanner {
    private final ArrayList<Integer> tokens;
    private final ArrayList<String> tokenStrings;
    private int index;

    private final HashSet<String> specialSymbols;
    private final HashMap<String, Integer> codeMappings;

    public CoreScanner(String file) throws IOException {
        // Instantiate buffered reader
        BufferedReader reader = new BufferedReader(new FileReader(file));

        // Initialize tokens and index
        tokens = new ArrayList<Integer>();
        tokenStrings = new ArrayList<String>();
        index = 0;

        // Initialize specialSymbols
        specialSymbols = new HashSet<String>();
        populateList(specialSymbols, "data/specialSymbols.txt");

        // Initialize codeMappings
        codeMappings = new HashMap<String, Integer>();

        // Continually call tokenizeLine until it returns -1 (reaches EOS)
        int returnedCode;
        do{
            returnedCode = tokenizeLine(reader);
        } while(returnedCode!= -1);
    }

    private void populateList(HashSet<String> s, String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String currLine = reader.readLine();
        while(currLine != null){
            s.add(currLine);
            currLine = reader.readLine();
        }
    }

    private int tokenizeLine(BufferedReader reader) throws IOException {
        String currLine = reader.readLine();
        while(currLine != null && currLine.trim().isEmpty()) {
            currLine = reader.readLine();
        }
        if(currLine == null){
            return -1;
        }
        currLine = normalizeLine(currLine);
        int position = 0;
        while(position < currLine.length()){
            String tokenOrSpaces = nextTokenOrSpaces(currLine, position);
            if(!tokenOrSpaces.trim().isEmpty()){
                tokenStrings.add(tokenOrSpaces);
                // Add token code
            }
            position += tokenOrSpaces.length();
        }
        System.out.println(tokenStrings);
        return 1;
    }

    private String normalizeLine(String line){
        StringBuilder str = new StringBuilder();
        for(String curr : specialSymbols){
            line = line.replace(curr, " "+curr+" ");
        }
        return line+" ";
    }

    private String nextTokenOrSpaces(String line, int position){
        int i = position;
        boolean isSpace = line.charAt(i) == ' ' || line.charAt(i) == '\t';
        while (i < line.length() &&
                isSpace == (line.charAt(i) == ' ' || line.charAt(i) == '\t')) {
            i++;
        }
        return line.substring(position, i);
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