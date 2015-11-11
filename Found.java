import java.util.ArrayList;


//This class is used to store the results of a SearchTask to the main program
public class Found {
    public String filename;
    public ArrayList<String> results;

    public Found(String filename, ArrayList<String> results){
        this.filename = filename;
        this.results = results;
    }

}
