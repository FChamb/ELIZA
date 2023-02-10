import java.util.ArrayList;

public class Keyword {
    private final String word;
    private final int priority;
    private ArrayList<Decomposition> decomposition;

    /**
     * This class is an object Keyword which can store multiple decompositions, a priority,
     * and the keyword. The class takes two variables, word and priority.
     * @param word - the actual word of the Keyword
     * @param priority - the priority of the Keyword
     */
    public Keyword(String word, int priority) {
        this.word = word;
        this.priority = priority;
    }

    /**
     * This is the second instance of a setter in the object classes, and it exists to make
     * creating a keyword in the engine possible.
     * @param decomposition - an arraylist of decompositions
     */
    public void setDecomposition(ArrayList<Decomposition> decomposition) {
        this.decomposition = decomposition;
    }

    /**
     * A simple getter which returns the keyword word.
     * @return - returns the String which is the Keyword word
     */
    public String getWord() {
        return word;
    }

    /**
     * Another simple getter which returns the priority of a keyword.
     * @return - returns an integer with the priority of the keyword
     */
    public int getPriority() { return priority; }

    /**
     * Another simple getter which returns an ArrayList of decompositions.
     * @return - returns the arraylist of decompositions
     */
    public ArrayList<Decomposition> getDecomposition() { return decomposition; }

    /**
     * This method acts as an override for the default toString, so that Keyword can
     * print out its variables properly.
     * @return - returns a nicely formatted string with the word, priority, and list of decompositions
     */
    public String toString() {
        return this.word + " " + this.getPriority() + "\n" + decomposition.toString();
    }
}
