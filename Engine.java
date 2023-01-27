import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Engine {
    private final ArrayList<String> welcomeMessages = new ArrayList<String>();
    private final ArrayList<String> quit = new ArrayList<String>();
    private final ArrayList<String> closingMessages = new ArrayList<String>();
    private final ArrayList<Substitution> preSubs = new ArrayList<Substitution>();
    private final ArrayList<Substitution> postSubs = new ArrayList<Substitution>();
    private ArrayList<Keyword> keywords = new ArrayList<Keyword>();
    private boolean programAlive = true;

    public void run(String file) throws FileNotFoundException {
        try {
            Scanner scan = new Scanner(new FileReader(file));
            scan.nextLine();
            startingMessages(scan);
            goodbyeMessages(scan);
            quitMessages(scan);
            preSubstitutionRules(scan);
            postSubstitutionRules(scan);
            generateKeywords(scan);
            sortKeywords();
        } catch (FileNotFoundException e) {
            System.out.println("Script file not found: " + e);
        }
    }

    private void startingMessages(Scanner scan) {
        String line = scan.nextLine();
        while (line.startsWith("start: ")) {
            int index = line.indexOf(":");
            this.welcomeMessages.add(line.substring(index + 2));
            line = scan.nextLine();
        }
    }
    private void goodbyeMessages(Scanner scan) {
        String line = scan.nextLine();
        while (line.startsWith("end: ")) {
            int index = line.indexOf(":");
            this.closingMessages.add(line.substring(index + 2));
            line = scan.nextLine();
        }
    }
    private void quitMessages(Scanner scan) {
        String line = scan.nextLine();
        while (line.contains("quit")) {
            int index = line.indexOf(":");
            this.quit.add(line.substring(index + 2));
            line = scan.nextLine();
        }
    }
    private void preSubstitutionRules(Scanner scan) {
        String line = scan.nextLine();
        while (line.contains("pre")) {
            String[] words = line.split(" ");
            this.preSubs.add(new Substitution(words[1], words[2]));
            line = scan.nextLine();
        }
    }

    public void postSubstitutionRules(Scanner scan) {
        String line = scan.nextLine();
        while (line.contains("post")) {
            String[] words = line.split(" ");
            this.postSubs.add(new Substitution(words[1], words[2]));
            line = scan.nextLine();
        }
    }

    private void generateKeywords(Scanner scan) {
        String keyword = null;
        int priority = 0;
        ArrayList<Decomposition> decompositions = new ArrayList<Decomposition>();
        ArrayList<String> reassembly = new ArrayList<String>();
        int index = 0;
        int dIndex = 0;
        String line = scan.nextLine();
        while (scan.hasNext()) {
            if (line.startsWith("reassembly: ")) {
                String reassemb = line.substring(line.indexOf(":") + 2);
                reassembly.add(reassemb);
            }
            if (line.startsWith("decomposition: ")) {
                if (!reassembly.isEmpty()) {
                    decompositions.get(dIndex).setReassembly(reassembly);
                    reassembly = new ArrayList<String>();
                    dIndex++;
                }
                String decomp = line.substring(line.indexOf(":") + 2);
                decompositions.add(new Decomposition(decomp));
            }
            if (line.startsWith("keyword: ")) {
                String[] words = line.split(" ");
                keyword = words[1];
                priority = Integer.parseInt(words[2]);
                dIndex = 0;
            }
            if (line.startsWith("-")) {
                if (!reassembly.isEmpty()) {
                    decompositions.get(dIndex).setReassembly(reassembly);
                    reassembly = new ArrayList<String>();
                    dIndex++;
                }
                this.keywords.add(new Keyword(keyword));
                this.keywords.get(index).setPriority(priority);
                this.keywords.get(index).setDecomposition(decompositions);
                index++;
                decompositions = new ArrayList<Decomposition>();
            }
            line = scan.nextLine();
        }
    }
}

