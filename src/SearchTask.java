import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class SearchTask implements Callable<Found> {
    //Stores the filename
    private String file;
    //Stores the search regex
    private String searchRegex;

    public SearchTask(String file, String searchRegex) {
        this.file = file;
        this.searchRegex = searchRegex;
    }

    //This method will determine if a line within a file contains the REGEX and
    // returns a boolean to represent the success
    @Override
    public Found call() throws Exception {
        //Execute the search and return the result
        return this.SearchFile();
    }

    // This method returns true if the file contains the search regex
    private Found SearchFile() {
        //counter to keep track of the file line
        int lineCounter = 0;
        //Used to store each line in the file to perform the regex search on
        String lineText;

        ArrayList<String> results = new ArrayList<String>();

        //Use a BufferedReader to read each line in the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            while ((lineText = reader.readLine()) != null) {

                //Append the line of the file
                lineCounter++;

                //if the line of the file matches the regex
                if (Pattern.matches(this.searchRegex, lineText)) {
                    //Add the line number and the lines contents to the results list
                    results.add(lineCounter + ": " + lineText);
                }
            }
        } catch (IOException e) {
            //Something IO related messed up
            System.out.println("INVALID FILENAME");
            System.out.println("The Filename: '" + this.file + "' Does not exist.");

            //FOR DEBUGING
            //e.printStackTrace();
        }

        //construct the result object to return to the main program
        return new Found(this.file, results);
    }
}
