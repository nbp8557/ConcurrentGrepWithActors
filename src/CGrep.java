import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import akka.actor.Actors.*;

public class CGrep{
    //Used for the thread pool
    private final ExecutorService threadPool;
    //Service for storing the results of a threads execution
    private final ExecutorCompletionService<Found> threadResults;
    //String array to store file names temporarily
    private String[] files;
    //String to store the regex search pattern temporarily
    private String searchPattern;

    public CGrep(int numberOfThreads, String[] files, String searchPattern){
        this.threadPool = Executors.newFixedThreadPool(numberOfThreads);
        this.threadResults = new ExecutorCompletionService<Found>(threadPool);
        this.files = files;
        this.searchPattern = searchPattern;
    }

    //Search all of the files
    public void SearchAllFiles(){
        //An arrayList to store all of the results
        ArrayList<Future<Found>> results = new ArrayList<Future<Found>>();


        int counter = 0;
        //Loop through each file and queue up the tasks to the thread pool
        for(String file: this.files){
           counter++;
        }
        //Create FileCount object
        FileCount fileCount = new FileCount(counter);

        //Create the collection Actor
        CollectionActor collectionActor = new CollectionActor();

        //Send the fileCount message to the collectionActor













        //Tell the thread pool to shutdown
        //NOTE: This shutdown method will wait for all tasks to complete
        this.threadPool.shutdown();
    }

    public static void main(String[] args) {
        //file stores the name of the file to perform the regex search on
        String[] file = new String[args.length];
        //searchPattern Stores the search regex
        String searchPattern;

        //Check if there are invalid arguments
        if(args.length < 2){
            System.out.println("INVALID ARGUMENTS");
            System.out.println("Usage: java CGrep PATTERN [File ... ]");
            System.exit(1);
        }

        //Store the search regex
        searchPattern = args[0];

        //Loop through the files and store them in the file array
        for(int counter=1; counter < args.length ; counter++){
            //Add filename to the file array
            file[counter-1]=args[counter];
        }

        //Check that the user entered regex is valid
        try{
            Pattern.compile(searchPattern);
        }catch (PatternSyntaxException exception){
            //The user entered an invalid regex pattern
            //Notify him and exit the program
            System.out.println("INVALID REGEX");
            System.exit(1);
        }

        //initialize the CGrep object with 3 threads
        CGrep grep = new CGrep(3,file,searchPattern);

        //Search all of the files
        grep.SearchAllFiles();
    }


}
