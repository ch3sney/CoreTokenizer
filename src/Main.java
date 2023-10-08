import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Instantiate a scanner
        CoreScanner scanner = new CoreScanner(args[0]);

        // While ending token is not encountered, continually skipToken and print
        while (scanner.getToken() != 33 && scanner.getToken() != 34) {
            System.out.println(scanner.getToken());
            scanner.skipToken();
        }
        // Print final token
        System.out.println(scanner.getToken());

        // Print appropriate message to console
        if (scanner.getToken() == 33) {
            System.out.println("SCANNER: Reached end of stream.");
        } else if (scanner.getToken() == 34) {
            System.out.println("SCANNER: Error encountered.");
        }
    }
}
