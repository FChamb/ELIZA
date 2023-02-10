public class Substitution {
    private final String before;
    private final String after;

    /**
     * This class acts as a substitution object which takes two string variables, before and after.
     * @param before - the word before the substitution
     * @param after -  the word after the substitution
     */
    public Substitution(String before, String after) {
        this.before = before;
        this.after = after;
    }

    /**
     * A simple getter which returns the before string variable.
     * @return - returns the String containing the before word.
     */
    public String getBefore() {
        return this.before;
    }

    /**
     * Another simple getter which returns the after string variable.
     * @return - return the String containing the after word.
     */
    public String getAfter() {
        return this.after;
    }

    /**
     * This method acts as an override of an object's default toString method in order
     * to properly print out the substitution. It returns the before followed by a space,
     * followed by the after.
     * @return - returns a nicely formatted string with before and after.
     */
    public String toString() { return before + " " + after; }
}

