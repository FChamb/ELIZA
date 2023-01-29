import java.io.FileNotFoundException;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.Scanner;

public class RunEngine {
    private final String eliza = "Eliza: ";
    private final String user = ">>";

    public static void main(String[] args) {
        try {
            Engine engine = new Engine();
            engine.run(args[0]);
            RunEngine run = new RunEngine();
            run.beginEliza(engine);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please provide a script for the engine: " + e);
        } catch (FileNotFoundException e) {
            System.out.println("That script file can not be found: " + e);
        }
    }

    public void beginEliza(Engine engine) {
        Scanner scan = new Scanner(System.in);
        System.out.print(eliza);
        engine.getWelcomeMesssages();
        while (engine.getProgramALive()) {
            System.out.print(user);
            String line = scan.nextLine().toLowerCase();
            line = line.replaceAll("[.,?!]", "");
            if (!engine.checkQuitMessages(line)) {
                break;
            }
            System.out.print(eliza);
            System.out.println(engine.generateResponse(line));
        }
        System.out.print(eliza);
        engine.getClosingMessages();
    }
}

