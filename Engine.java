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

    public void sortKeywords() {
        ArrayList<Keyword> sorted = new ArrayList<Keyword>();
        int[] priorities = new int[keywords.size()];
        for (int i = 0; i < priorities.length; i++) {
            priorities[i] = keywords.get(i).getPriority();
        }
        Arrays.sort(priorities);
        for (int i = 0; i < priorities.length; i++) {
            for (int j = 0; j < keywords.size(); j++) {
                if (keywords.get(j).getPriority() == priorities[i]) {
                    sorted.add(keywords.get(j));
                    keywords.remove(keywords.get(j));
                    break;
                }
            }
        }
        keywords = sorted;
    }

    public void getWelcomeMesssages() {
        System.out.println(welcomeMessages.get(randomElement(welcomeMessages)));
    }

    public void getClosingMessages() {
        System.out.println(closingMessages.get(randomElement(closingMessages)));
    }


    public boolean checkQuitMessages(String line) {
        for (int i = 0; i < quit.size(); i++) {
            if (line.contains(quit.get(i))) {
                this.programAlive = false;
                return true;
            }
        }
        return false;
    }

    public String generateResponse(String line, String[] words) {
        ArrayList<Keyword> possibleKeyword = new ArrayList<Keyword>();
        ArrayList<String> responses = new ArrayList<String>();
        String[] responseWords;
        String takenFromLine = "";
        String response = "";
        String preSubLine = correctPreSubs(words);
        for (String word : words) {
            ArrayList<Keyword> key = isWordKeyword(word);
            if (key.size() != 0) {
                possibleKeyword.addAll(key);
            }
        }
        Keyword highest = getHighestPriority(possibleKeyword);
        responses = checkDecomposition(preSubLine, highest);
        responses.addAll(checkReferenceTo(responses));
        response = responses.get(randomElement(responses));
        responseWords = response.split(" ");
        for (String word : responseWords) {
            word = word.replaceAll("[.,?!]", "");
            if (word.contains("(r)")) {
                int index = line.indexOf(highest.getWord());
                //System.out.println(index);
                //System.out.println(line);
                //System.out.println(response);
                takenFromLine = line.substring(index);
                takenFromLine = correctPostSubs(takenFromLine);
                response = response.replace("(r)", takenFromLine);
                return response;
            }
        }
        return response;
    }

    public ArrayList<Keyword> isWordKeyword(String word) {
        ArrayList<Keyword> possibleKeywords = new ArrayList<Keyword>();
        for (Keyword key : keywords) {
            if (key.getWord().equals(word) || key.getWord().equals("NONE")) {
                possibleKeywords.add(key);
            }
        }
        return possibleKeywords;
    }

    public ArrayList<String> checkDecomposition(String line, Keyword keyword) {
        for (Decomposition decomp : keyword.getDecomposition()) {
            if (line.contains(decomp.getDecomposition())) {
                return decomp.getReassembly();
            }
        }
        return keyword.getDecomposition().get(0).getReassembly();
    }

    public ArrayList<String> checkReferenceTo(ArrayList<String> responses) {
        for (String resp : responses) {
            if (resp.contains("referto")) {
                String newResp = resp.substring(resp.indexOf(" ") + 1);
                for (Keyword key : keywords) {
                    if (key.getWord().equals(newResp)) {
                        responses.remove(resp);
                        responses.add(key.getDecomposition().get(0).getReassembly().get(randomElement(key.getDecomposition().get(0).getReassembly())));
                    }
                }
            }
        }
        return responses;
    }

    public Keyword getHighestPriority(ArrayList<Keyword> keywords) {
        int highest = 0;
        Keyword highestKey = null;
        for (Keyword key : keywords) {
            if (key.getPriority() >= highest) {
                highest = key.getPriority();
                highestKey = key;
            }
        }
        return highestKey;
    }

    public String correctPreSubs(String[] words) {
        String[] newWords = new String[words.length];
        for (int i = 0; i < newWords.length; i++) {
            newWords[i] = words[i];
        }
        String finalLine = "";
        for (int i = 0; i < words.length; i++) {
            for (Substitution sub : preSubs) {
                if (words[i].contains(sub.getBefore())) {
                    newWords[i] = sub.getAfter();
                    break;
                } else {
                    newWords[i] = words[i];
                }
            }
        }
        for (String word : newWords) {
            finalLine += (word + " ");
        }
        return finalLine;
    }

    public String correctPostSubs(String line) {
        String[] words = line.split(" ");
        String finalLine = "";
        for (int i = 0; i < words.length; i++) {
            for (Substitution sub : postSubs) {
                if (words[i].contains(sub.getBefore())) {
                    words[i] = sub.getAfter();
                    break;
                }
            }
        }
        for (String word : words) {
            finalLine += (word + " ");
        }
        System.out.println(finalLine);
        return finalLine;
    }

    public boolean getProgramALive() {
        return this.programAlive;
    }

    public int randomElement(ArrayList list) {
        return (int) (Math.random() * list.size());
    }
}

