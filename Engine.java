import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The engine class. This acts as the groundwork to enable Eliza to properly work. It contains all the required methods
 * to read a script, create a reply and everything in-between.
 */
public class Engine {
    private final ArrayList<String> welcomeMessages = new ArrayList<String>();
    private final ArrayList<String> quit = new ArrayList<String>();
    private final ArrayList<String> closingMessages = new ArrayList<String>();
    private final ArrayList<Substitution> preSubs = new ArrayList<Substitution>();
    private final ArrayList<Substitution> postSubs = new ArrayList<Substitution>();
    private ArrayList<Keyword> keywords = new ArrayList<Keyword>();
    private ArrayList<ArrayList<String>> memories = new ArrayList<ArrayList<String>>();
    private boolean programAlive = true;

    /**
     * The run method is what is called to in RunEngine to scan through a script and store all the
     * various Keywords, welcome and closing messages, and pre/post-substitutions. It takes a String
     * variable which stores the script file name.
     */
    public void run(String file) throws FileNotFoundException {
        try {
            Scanner scan = new Scanner(new FileReader(file));
            /**
             * The method creates a scanner and passes it to each of the methods below which will
             * populate the private ArrayLists with the appropriate script values.
             */
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

    /**
     * This method scans through the scanner and looks for every instance of "start: ". When that is found,
     * everything after start is added to the welcoming messages.
     */
    private void startingMessages(Scanner scan) {
        String line = scan.nextLine();
        while (line.startsWith("start: ")) {
            int index = line.indexOf(":");
            this.welcomeMessages.add(line.substring(index + 2));
            line = scan.nextLine();
        }
    }

    /**
     * This method scans through the scanner and looks for every instance of "end: ". When that is found,
     * everything after end is added to the closing messages.
     */
    private void goodbyeMessages(Scanner scan) {
        String line = scan.nextLine();
        while (line.startsWith("end: ")) {
            int index = line.indexOf(":");
            this.closingMessages.add(line.substring(index + 2));
            line = scan.nextLine();
        }
    }

    /**
     * This method scans through the scanner and looks for every instance of "quit: ". When that is found,
     * everything after quit is added to the quit messages.
     */
    private void quitMessages(Scanner scan) {
        String line = scan.nextLine();
        while (line.contains("quit: ")) {
            int index = line.indexOf(":");
            this.quit.add(line.substring(index + 2));
            line = scan.nextLine();
        }
    }

    /**
     * This method scans through the scanner and looks for every instance of "pre: ". When that is found,
     * it splits the line into individual words using a regex split. Then, if the amount of words is greater
     * than three, the program will create a new substitution with the first words as before and the other
     * words as after. If the amount of words is only two, a new substitution is created with a before
     * and after.
     */
    private void preSubstitutionRules(Scanner scan) {
        String line = scan.nextLine();
        while (line.contains("pre")) {
            String[] words = line.split(" ");
            if (words.length > 3) {
                this.preSubs.add(new Substitution(words[1], words[2] + " " + words[3]));
            } else {
                this.preSubs.add(new Substitution(words[1], words[2]));
            }
            line = scan.nextLine();
        }
    }

    /**
     * This method scans through the scanner and looks for every instance of "post: ". When that is found,
     * it splits the line into individual words using a regex split. A new substitution is created with a
     * before and after.
     */
    public void postSubstitutionRules(Scanner scan) {
        String line = scan.nextLine();
        while (line.contains("post")) {
            String[] words = line.split(" ");
            this.postSubs.add(new Substitution(words[1], words[2]));
            line = scan.nextLine();
        }
    }

    /**
     * This method is the final method in reading the script. It scans every line and looks for each instance
     * of, first "reassembly: ", then "decomposition: ", and finally "keyword: ". If one of those are matched,
     * it begins the cycle of creating a keyword with different decomposition and reassembly rules.
     */
    private void generateKeywords(Scanner scan) {
        /**
         * Instance variables of keyword and priority are created so the method can
         * access them throughout.
         *
         * Decomposition and reassembly arraylists are created so that the method can access
         * them when creating a keyword.
         */
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
                /**
                 * There is a check to first see if the reassembly arraylist is full. If it is, then
                 * that means that this is a new decomposition and the method should start the cycle over.
                 */
                if (!reassembly.isEmpty()) {
                    decompositions.get(dIndex).setReassembly(reassembly);
                    reassembly = new ArrayList<String>();
                    dIndex++;
                }
                /**
                 * If the above check did not find anything, then it created a new decomposition so that
                 * the reassembly rules can be found.
                 */
                String decomp = line.substring(line.indexOf(":") + 2);
                decompositions.add(new Decomposition(decomp));
            }
            if (line.startsWith("keyword: ")) {
                String[] words = line.split(" ");
                keyword = words[1];
                priority = Integer.parseInt(words[2]);
                dIndex = 0;
            }
            /**
             * In the script each Keyword ends with a "-" so that the program can know that this is the
             * end of the cycle and that the keyword should have its decomposition and reassembly rules
             * set.
             */
            if (line.startsWith("-")) {
                if (!reassembly.isEmpty()) {
                    decompositions.get(dIndex).setReassembly(reassembly);
                    reassembly = new ArrayList<String>();
                    dIndex++;
                }
                this.keywords.add(new Keyword(keyword, priority));
                this.keywords.get(index).setDecomposition(decompositions);
                index++;
                decompositions = new ArrayList<Decomposition>();
            }
            line = scan.nextLine();
        }
    }

    /**
     * This method looks at the private arraylist of keywords and uses two for loops to sort first
     * an array of the priorities, and then the actual arraylist of keywords. The output of this method
     * changes the private arraylist to a sorted list with the highest numbers at the start.
     */
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

    /**
     * This is a simple getter like method which prints out a random welcome message for the
     * run engine class.
     */
    public void getWelcomeMesssages() {
        System.out.println(welcomeMessages.get(randomElement(welcomeMessages)));
    }

    /**
     * This is another simple getter like method which prints out a random closing message
     * for the run engine class.
     */
    public void getClosingMessages() {
        System.out.println(closingMessages.get(randomElement(closingMessages)));
    }

    /**
     * This method takes the user input, splits it into individual words and checks to
     * see if any of the words in the response contain one of the quit messages. If the line
     * does, then the private variable programAlive is set to false and a boolean false is
     * returned to RunEngine.
     */
    public boolean checkQuitMessages(String line) {
        String[] words = line.split(" ");
        for (int i = 0; i < quit.size(); i++) {
            for (String word : words) {
                if (word.equals(quit.get(i))) {
                    this.programAlive = false;
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method is the backbone to the Eliza engine. It takes a user input line and creates a valid response,
     * by looking at what the user says. It finds the highest keyword, gets the correct decomposition and chooses
     * a random reassembly to respond. If the reassembly calls back to the user input then the method will cut the
     * correct portion of the user input and paste it into the response.
     */
    public String generateResponse(String line) {
        ArrayList<Keyword> possibleKeyword = new ArrayList<Keyword>();
        ArrayList<String> responses = new ArrayList<String>();
        String[] responseWords;
        String response = "";
        String[] oldWords = line.split(" ");
        /**
         * First an array of the old words is created from the user input. Then a new string variable is created
         * that gets the output from the method correctPreSubs. This method checks every word in a line and replaces
         * every pre-substitution from the script.
         */
        String preSubLine = correctPreSubs(line);
        String[] newWords = preSubLine.split(" ");
        /**
         * After the substitution has been made, every word in the new line is evaluated to see if it is a Keyword.
         * If it is, it is added to an arraylist for future evaluation.
         */
        for (String word : newWords) {
            ArrayList<Keyword> key = isWordKeyword(word);
            if (key.size() != 0) {
                possibleKeyword.addAll(key);
            }
        }
        /**
         * Once all the keywords have been found from the user input, the one with the highest priority is found.
         * Next the checkDecomposition method is called which finds what decomposition rule is used in the user input.
         * The return value of that method is an arraylist of responses. After the reassembly rules are found the
         * method finds if any of the responses have reference to a different keyword. This is part of the
         * synonym implementation which enables specific words to relate to others. Finally, a random response is
         * chosen from the list of available ones.
         */
        Keyword highest = getHighestPriority(possibleKeyword);
        responses = checkDecomposition(preSubLine, highest);
        checkReferenceTo(preSubLine, responses);
        response = responses.get(randomElement(responses));
        responseWords = response.split(" ");
        /**
         * Lastly this method scrolls through every word of the response and checks for "(r)". This is a reference
         * in the response that the program needs to take part of the user input for the response. The conditional
         * statement checks if the decomposition rule has a "<" at the end, which symbolises that the method takes
         * everything after the keyword.
         * After having created a response the program will make sure the keyword is not an emotion. If it is not,
         * then a memory will be created for future conversation.
         */
        for (String word : responseWords) {
            if (word.contains("(r)")) {
                if (highest.getDecomposition().toString().contains("<")) {
                    int index = preSubLine.indexOf(highest.getWord());
                    int difference = correctIndexError(oldWords, newWords, highest);
                    String takenFromLine = line.substring((index + highest.getWord().length() + 1) - difference);
                    takenFromLine = correctPostSubs(takenFromLine);
                    response = response.replace("(r)", takenFromLine);
                    if (!highest.getWord().equals("emotion")){
                        createMemory(highest, response);
                    }
                } else {
                    int index = preSubLine.indexOf(highest.getWord());
                    int difference = correctIndexError(oldWords, newWords, highest);
                    String takenFromLine = line.substring(index - difference);
                    takenFromLine = correctPostSubs(takenFromLine);
                    response = response.replace("(r)", takenFromLine);
                    if (!highest.getWord().equals("emotion")){
                        createMemory(highest, response);
                    }
                }
            }
        }
        return response;
    }

    /**
     * This method acts a helper method to generateResponse. It takes a string variable which contains one word,
     * and cycles through all the keywords. If the word matches it adds it to an arraylist and returns the list
     * after checking all possibilities.
     */
    public ArrayList<Keyword> isWordKeyword(String word) {
        ArrayList<Keyword> possibleKeywords = new ArrayList<Keyword>();
        for (Keyword key : keywords) {
            if (key.getWord().equals(word) || key.getWord().equals("NONE")) {
                possibleKeywords.add(key);
            }
        }
        return possibleKeywords;
    }

    /**
     * This method takes in the user input and the highest keyword in that line. It cycles through every
     * decomposition in the highest keyword and checks to see if the user input has the corresponding decomposition
     * in it. If the input does match one of the decompositions, it checks to see if the keyword is equal to "NONE".
     * What this means is that no keyword fits in the output and a default response is chosen. If a default response
     * is chosen then there is a 50% chance that a memory will be chosen, assuming that the program has created
     * at least one memory. If nothing matches, a default message is chosen.
     */
    public ArrayList<String> checkDecomposition(String line, Keyword keyword) {
        int random = (int) (Math.random() * 10);
        for (Decomposition decomp : keyword.getDecomposition()) {
            if (keyword.getWord().equals("NONE") && !memories.isEmpty() && random <= 5) {
                ArrayList<String> memory = memories.get(randomElement(memories));
                memories.remove(memory);
                return memory;
            } else if (line.contains(decomp.getDecomposition().replaceAll("<", ""))) {
                return decomp.getReassembly();
            } else if (decomp.getDecomposition().equals("NONE")) {
                return keyword.getDecomposition().get(0).getReassembly();
            }
        }
        return keywords.get(0).getDecomposition().get(0).getReassembly();
    }

    /**
     * This method acts as a sort of synonym checker. Certain keywords/decompositions have very similar
     * responses, and because of that each script has special tokens to refer to other keywords. This method
     * takes the user input and a list of the found responses. It looks at every response and sees if any of
     * them contain the referto token. If the line contain a referto, then the new keyword is set to the reference
     * and checkDecomposition gets the appropriate responses.
     */
    public void checkReferenceTo(String line, ArrayList<String> responses) {
        for (int i = 0; i < responses.size(); i++) {
            String resp = responses.get(i);
            if (resp.contains("referto")) {
                String newResp = resp.substring(resp.indexOf(" ") + 1);
                for (Keyword key : keywords) {
                    if (key.getWord().equals(newResp)) {
                        responses.remove(resp);
                        responses.addAll(checkDecomposition(line, key));
                    }
                }
            }
        }
    }

    /**
     * This method takes a list of possible keywords and checks which one has the highest priority.
     * It does this by using a for loop to find every keyword's word and priority. If the priority of the
     * next word is higher than the previous, than the highest is set to the new word.
     */
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

    /**
     * This method takes the user input line and splits it into its individual words. If any of the
     * words are equal to any of the pre-substitution rules, then the word is replaced with the proper
     * word. Finally, the line is rebuilt and returned.
     */
    public String correctPreSubs(String line) {
        String[] words = line.split(" ");
        String finalLine = "";
        for (int i = 0; i < words.length; i++) {
            for (Substitution sub : preSubs) {
                if (words[i].equals(sub.getBefore())) {
                    words[i] = sub.getAfter();
                    break;
                } else {
                    words[i] = words[i];
                }
            }
        }
        for (String word : words) {
            finalLine += (word + " ");
        }
        return finalLine;
    }

    /**
     * This method takes the generateResponse line and splits it into its individual words. If any of the
     * words are equal to any of the post-substitution rules, then the word is replaced with the proper
     * word. Finally, the line is rebuilt and returned.
     */
    public String correctPostSubs(String line) {
        String[] words = line.split(" ");
        String finalLine = "";
        for (int i = 0; i < words.length; i++) {
            for (Substitution sub : postSubs) {
                if (words[i].equals(sub.getBefore())) {
                    words[i] = sub.getAfter();
                    break;
                }
            }
        }
        for (int i = 0; i < words.length - 1; i++) {
            finalLine += (words[i] + " ");
        }
        finalLine += words[words.length - 1];
        return finalLine;
    }

    /**
     * This method is essential to allowing the Engine take the proper section of the user input.
     * It takes arrays with both the old and new words as well as the highest keyword. The old words
     * are the user's original input. The new words are the words after pre-substitutions have been made.
     * Because pre-substitution words may have different lengths this program compares each word from the
     * original input with its counterpart. If they are different length, then difference adds the length
     * difference. At the end the difference is returned.
     */
    public int correctIndexError(String[] oldWords, String[] newWords, Keyword highest) {
        int difference = 0;
        for (int i = 0; i < newWords.length; i++) {
            if (newWords[i].equals(highest.getWord())) {
                break;
            }
            if (!oldWords[i].equals(newWords[i])) {
                if (!newWords[i].equals(highest.getWord())) {
                    difference += newWords[i].length() - oldWords[i].length();
                } 
            }
        
        }
        return difference;
    }

    /**
     * This method creates a memory using the highest keyword and a previously asked question. Two generic
     * responses are created which pursues the user about a previously mentioned topic. As long as the highest
     * keyword does not equal "NONE", then the memory is added for future use.
     */
    public void createMemory(Keyword keyword, String takenFromLine) {
        ArrayList<String> memoryResponses = new ArrayList<String>();
        takenFromLine = takenFromLine.substring(0, takenFromLine.length() - 1).toLowerCase();
        memoryResponses.add("Earlier I asked you " + takenFromLine + ". Would you like to discuss this further?");
        memoryResponses.add("Let's talk more about the question I asked you earlier, " + takenFromLine + ".");
        if (!keyword.getWord().equals("NONE")) {
            memories.add(memoryResponses);
        }
    }

    /**
     * getProgramAlive is a simple getter method that returns the private boolean value which is by default
     * set to true. If the user enters a quit word, then programAlive is set to false.
     */
    public boolean getProgramAlive() {
        return this.programAlive;
    }

    /**
     * randomElement takes an arraylist input and returns a random int value contained in the list.
     * This is used for picking random welcome/closing messages and responses.
     */
    public int randomElement(ArrayList list) {
        return (int) (Math.random() * list.size());
    }
}