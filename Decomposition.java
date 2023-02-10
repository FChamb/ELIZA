import java.util.ArrayList;

public class Decomposition {
    private final String decomposition;
    private ArrayList<String> reassembly;

    /**
     * This class acts as a decomposition object which takes a string variable, decomposition.
     * Decomposition is a string variable which holds a keyword's decomposition.
     * @param decomposition - the word/phrase that defines the decomposition rule
     */
    public Decomposition(String decomposition) {
        this.decomposition = decomposition;
    }

    /**
     * This method acts as a setter method which takes an ArrayList of strings and sets
     * the reassembly rules of this particular instance of reassembly. This is one of the two instances of
     * a setter in all the objects created, because when creating a keyword in the engine this proved essential.
     * @param reassembly - the arraylist of responses for a given decomposition
     */
    public void setReassembly(ArrayList<String> reassembly) {
        this.reassembly = reassembly;
    }

    /**
     * A simple getter method which returns the string variable decomposition.
     * @return - returns the String that contains the decomposition rule
     */
    public String getDecomposition() {
        return decomposition;
    }

    /**
     * Another simple getter method which returns the ArrayList of strings, reassembly.
     * @return - returns the arraylist of reassembly rules
     */
    public ArrayList<String> getReassembly() {
        return reassembly;
    }

    /**
     * This method acts as an override to the default toString and enables the object to properly
     * return everything it contains.
     * @return - returns a nicely formatted String with the decomposition and reassembly rules
     */
    public String toString() {
        return decomposition + " " + reassembly;
    }
}

