import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CoreScanner {
    /*
     * Private members --------------------------------------------------------
     */
    private final ArrayList<Integer> tokens;
    private final ArrayList<String> tokenStrings;
    private int index;

    private final HashSet<String> specialSymbols;
    private final HashMap<String, Integer> codeMappings;

    private void populateList(HashSet<String> s, String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String currLine = reader.readLine();
        while (currLine != null) {
            s.add(currLine);
            currLine = reader.readLine();
        }
    }

    private void populateMap(HashMap<String, Integer> m, String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String currLine = reader.readLine();
        while (currLine != null) {
            String[] split = currLine.split(" ");
            m.put(split[0], Integer.valueOf(split[1]));
            currLine = reader.readLine();
        }
    }

    private int tokenizeLine(BufferedReader reader) throws IOException {
        String currLine = reader.readLine();
        while (currLine != null && currLine.trim().isEmpty()) {
            currLine = reader.readLine();
        }
        if (currLine == null) {
            return -1;
        }
        currLine = normalizeSpacing(currLine);
        int position = 0;
        while (position < currLine.length()) {
            String tokenOrSpaces = nextTokenOrSpaces(currLine, position);
            if (!tokenOrSpaces.trim().isEmpty()) {
                int tokenCode = getTokenCode(tokenOrSpaces);
                if(tokenCode != 34) {
                    tokenStrings.add(tokenOrSpaces);
                    tokens.add(tokenCode);
                } else {
                    tokens.add(34);
                    break;
                }
            }
            position += tokenOrSpaces.length();
        }
        return 1;
    }

    private void greedify(){
        for
    }

    private int getTokenCode(String token) {
        if (codeMappings.containsKey(token)) {
            return codeMappings.get(token);
        } else if (isInteger(token)) {
            return 31;
        } else if (isIdentifier(token)) {
            return 32;
        } else {
            return 34;
        }
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isIdentifier(String str) {
        return Character.isLetter(str.charAt(0)) && str.matches("^[A-Z0-9]*$");
    }

    private String normalizeSpacing(String line) {
        StringBuilder str = new StringBuilder();
        for (String curr : specialSymbols) {
            line = line.replace(curr, " " + curr + " ");
        }
        return line + " ";
    }

    private String nextTokenOrSpaces(String line, int position) {
        int i = position;
        boolean isSpace = line.charAt(i) == ' ' || line.charAt(i) == '\t';
        while (i < line.length() &&
                isSpace == (line.charAt(i) == ' ' || line.charAt(i) == '\t')) {
            i++;
        }
        return line.substring(position, i);
    }

    /*
     * Constructors -----------------------------------------------------------
     */
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
        populateMap(codeMappings, "data/codeMappings.txt");

        // Continually call tokenizeLine until it returns -1 (reaches EOS)
        int returnedCode;
        do {
            returnedCode = tokenizeLine(reader);
        } while (returnedCode != -1);

        tokens.add(33);
        System.out.println(tokens.toString());
    }



    /*
     * Kernel methods ---------------------------------------------------------
     */
    public int getToken() {
        return 0;
    }

    public int intVal() {
        return 0;
    }

    public String idName() {
        return "";
    }

}