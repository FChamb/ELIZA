public class Substitution {
    private final String before;
    private final String after;

    /**
     * This class acts as a substitution object which takes two string variables, before and after.
     */
    public Substitution(String before, String after) {
        this.before = before;
        this.after = after;
    }

    /**
     * A simple getter which returns the before string variable.
     */
    public String getBefore() {
        return this.before;
    }

    /**
     * Another simple getter which returns the after string variable.
     */
    public String getAfter() {
        return this.after;
    }

    /**
     * This method acts as an override of an object's default toString method in order
     * to properly print out the substitution. It returns the before followed by a space,
     * followed by the after.
     */
    public String toString() { return before + " " + after; }
}

