import java.util.ArrayList;

public class Decomposition {
    private final String decomposition;
    private ArrayList<String> reassembly;

    public Decomposition(String decomposition) {
        this.decomposition = decomposition;
    }

    public void setReassembly(ArrayList<String> reassembly) {
        this.reassembly = reassembly;
    }

    public String getDecomposition() {
        return decomposition;
    }

    public ArrayList<String> getReassembly() {
        return reassembly;
    }

    public String toString() {
        return decomposition + " " + reassembly;
    }
}

