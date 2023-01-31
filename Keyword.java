import java.util.ArrayList;

public class Keyword {
    private final String word;
    private int priority;
    private ArrayList<Decomposition> decomposition;

    public Keyword(String word) {
        this.word = word;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDecomposition(ArrayList<Decomposition> decomposition) {
        this.decomposition = decomposition;
    }

    public String getWord() {
        return word;
    }

    public int getPriority() { return priority; }

    public ArrayList<Decomposition> getDecomposition() { return decomposition; }

    public String toString() {
        return this.word + " " + this.getPriority() + "\n" + decomposition.toString();
    }
}
