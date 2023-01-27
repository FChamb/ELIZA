public class Substitution {
    private final String before;
    private final String after;

    public Substitution(String before, String after) {
        this.before = before;
        this.after = after;
    }

    public String getBefore() {
        return this.before;
    }

    public String getAfter() {
        return this.after;
    }

    public String toString() { return before + " " + after; }
}

