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

    // BufferedReader for reading from file
    private final BufferedReader reader;

    // Represent tokens and corresponding strings
    public final ArrayList<Integer> tokens;
    private final ArrayList<String> tokenStrings;

    // Current token index
    private int index;

    // Store special symbols and mappings between keywords and token code
    private final HashSet<String> specialSymbols;
    private final HashMap<String, Integer> codeMappings;

    /*
     * Tokenizes a single line in provided file.
     */
    private void tokenizeLine(BufferedReader reader) throws IOException {
        // Store initial size of array.
        int initialSize = tokens.size();

        // Continue reading lines until non-empty or null found
        String currLine = reader.readLine();
        while (currLine != null && currLine.trim().isEmpty()) {
            currLine = reader.readLine();
        }

        // If line is null, add EOS token and return
        if (currLine == null) {
            tokens.add(33);
            tokenStrings.add("eos");
            return;
        }

        // "Normalize spacing" of line. Adds spaces between special symbols and to end of line.
        currLine = normalizeSpacing(currLine);

        int position = 0;
        while (position < currLine.length()) {
            // Extract a single token or white-space
            String tokenOrSpaces = nextTokenOrSpaces(currLine, position);

            // If tokenOrSpaces is not empty (is token candidate) continue...
            if (!tokenOrSpaces.trim().isEmpty()) {
                // Get corresponding token code
                int tokenCode = getTokenCode(tokenOrSpaces);

                // Check if token is a token (not an error)
                if (tokenCode != 34) {
                    tokenStrings.add(tokenOrSpaces);
                    tokens.add(tokenCode);
                } else {
                    // Break if error is found
                    tokenStrings.add("error");
                    tokens.add(34);
                    break;
                }
            }
            position += tokenOrSpaces.length();
        }
        // Combine tokens greedily
        greedifyLine(initialSize);
    }

    /*
     * Populates set with symbols in given file.
     */
    private void populateList(HashSet<String> s, String filePath) throws IOException {
        // Initialize reader
        BufferedReader listReader = new BufferedReader(new FileReader(filePath));

        String currLine = listReader.readLine();
        while (currLine != null) {
            // Add symbol to set
            s.add(currLine);
            currLine = listReader.readLine();
        }
        listReader.close();
    }

    /*
     * Populates map with symbols and corresponding codes.
     */
    private void populateMap(HashMap<String, Integer> m, String filePath) throws IOException {
        // Initialize reader
        BufferedReader mapReader = new BufferedReader(new FileReader(filePath));

        String currLine = mapReader.readLine();
        while (currLine != null) {
            // Add symbol and corresponding code to map
            String[] split = currLine.split(" ");
            m.put(split[0], Integer.valueOf(split[1]));
            currLine = mapReader.readLine();
        }

    }

    /*
     * Greedily combine adjacent tokens. Somewhat of a hardcoded approach IMO.
     */
    private void greedifyLine(int lineIndex) {
        // Iterate from starting index to end of array
        int i = lineIndex;

        while (i < tokens.size() - 1) {
            // Get adjacent tokens
            int token = tokens.get(i);
            int nextToken = tokens.get(i + 1);

            // Combine adjacent tokens which match criteria
            if (token == 15 && nextToken == 14) {
                greedySwap(i, 25, "!=");
            } else if (token == 14 && nextToken == 14) {
                greedySwap(i, 26, "!=");
            } else if (token == 27 && nextToken == 14) {
                greedySwap(i, 29, "<=");
            } else if (token == 28 && nextToken == 14) {
                greedySwap(i, 30, ">=");
            } else {
                i++;
            }
        }
    }

    /*
     * Remove two symbols and replace with single greedy token
     */
    private void greedySwap(int index, int code, String str) {
        // Update tokenStrings
        tokenStrings.remove(index + 1);
        tokenStrings.remove(index);
        tokenStrings.add(index, str);

        // Update tokens
        tokens.remove(index + 1);
        tokens.remove(index);
        tokens.add(index, code);
    }

    /*
     * Given string, return token code
     */
    private int getTokenCode(String token) {
        if (codeMappings.containsKey(token)) {
            // Is a mapped token
            return codeMappings.get(token);
        } else if (isInteger(token)) {
            // Is an integer
            return 31;
        } else if (isIdentifier(token)) {
            // Is an identifier
            return 32;
        } else {
            // Error!
            return 34;
        }
    }

    /*
     * Test if given string is integer (how is this not a string method already?!)
     */
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     * Test if given string matches criteria to be an identifier
     */
    private boolean isIdentifier(String str) {
        return Character.isLetter(str.charAt(0)) && str.matches("^[A-Z0-9]*$");
    }

    /*
     * Add spaces between special symbols and to end of line
     */
    private String normalizeSpacing(String line) {
        for (String curr : specialSymbols) {
            // Add spaces between special symbols
            line = line.replace(curr, " " + curr + " ");
        }

        // Add space to end of line
        return line + " ";
    }

    /*
     * Returns next token or white space
     */
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
        reader = new BufferedReader(new FileReader(file));

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
        tokenizeLine(reader);

        // Set index to 0
        index = 0;
    }


    /*
     * Kernel methods ---------------------------------------------------------
     */
    public int getToken() {
        // Return current token
        return tokens.get(index);
    }

    public void skipToken() throws IOException {
        // If at end of current line and token is not 33 or 34, tokenize a new line
        if (index == tokens.size() - 1 && tokens.get(index) != 33 && tokens.get(index) != 34) {
            tokenizeLine(reader);
        }

        // If token is not 33 or 34, increase index
        if (tokens.get(index) != 33 && tokens.get(index) != 34) {
            index++;
        }
    }

    public int intVal() {
        assert tokens.get(index) == 31 : "Violation of: current token is integer";

        // Return int value
        return Integer.parseInt(tokenStrings.get(index));
    }

    public String idName() {
        assert tokens.get(index) == 32 : "Violation of: current token is identifier";

        // Return identifier value
        return tokenStrings.get(index);
    }
}