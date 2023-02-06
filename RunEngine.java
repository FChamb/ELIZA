import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.Scanner;

public class RunEngine {
    /**
     * Starter characters to allow the program to print out in a more simplistic
     * and easy to view output.
     */
    private final String eliza = "Eliza: ";
    private final String user = ">>";
    private final String BLUE = "\u001B[34m";
    private final String RED = "\u001b[31m";
    private final String RESET = "\u001B[0m";

    /**
     * The RunEngine class acts as a kick-start for the Engine class. It creates a new engine
     * using the provided script and runs a scan through the script. Then it creates a new RunEngine
     * instance and begins Eliza. There are several catches to make sure the user inputs an appropriate
     * script file and that the file exists.
     */
    public static void main(String[] args) {
        try {
            Engine engine = new Engine();
            FileReader fileReader = new FileReader(args[0]);
            engine.run(fileReader);
            RunEngine run = new RunEngine();
            run.beginEliza(engine);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please provide a script for the engine: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("That script file can not be found: " + e.getMessage());
        }
    }

    /**
     * beginELiza is simply a while loops that continues to retrieve a user input. It starts
     * by providing a welcome message and then entering into the while loop. Each cycle it checks to
     * see if the user inputted a quit message, and then removes all punctuation so that the Engine can
     * generate a response. After quit has been called, a closing message is given and the program ends.
     */
    public void beginEliza(Engine engine) {
        Scanner scan = new Scanner(System.in);
        System.out.print(RED + eliza);
        engine.getWelcomeMesssages();
        while (engine.getProgramAlive()) {
            System.out.print(RESET + BLUE + user);
            String line = scan.nextLine().toLowerCase();
            line = line.replaceAll("[.,?!]", "");
            if (!engine.checkQuitMessages(line)) {
                break;
            }
            System.out.print(RESET + RED + eliza);
            System.out.println(engine.generateResponse(line));
        }
        System.out.print(RESET + RED + eliza);
        engine.getClosingMessages();
    }
}

