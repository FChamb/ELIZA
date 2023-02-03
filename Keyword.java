import java.util.ArrayList;

public class Keyword {
    private final String word;
    private final int priority;
    private ArrayList<Decomposition> decomposition;

    /**
     * This class is an object Keyword which can store multiple decompositions, a priority,
     * and the keyword. The class takes two variables, word and priority.
     */
    public Keyword(String word, int priority) {
        this.word = word;
        this.priority = priority;
    }

    /**
     * This is the second instance of a setter in the object classes, and it exists to make
     * creating a keyword in the engine possible.
     */
    public void setDecomposition(ArrayList<Decomposition> decomposition) {
        this.decomposition = decomposition;
    }

    /**
     * A simple getter which returns the keyword word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Another simple getter which returns the priority of a keyword.
     */
    public int getPriority() { return priority; }

    /**
     * Another simple getter which returns an ArrayList of decompositions.
     */
    public ArrayList<Decomposition> getDecomposition() { return decomposition; }

    /**
     * This method acts as an override for the default toString, so that Keyword can
     * print out its variables properly.
     */
    public String toString() {
        return this.word + " " + this.getPriority() + "\n" + decomposition.toString();
    }
}
