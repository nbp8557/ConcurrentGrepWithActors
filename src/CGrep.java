import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import akka.actor.ActorRef;
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
        //An arrayList to store all configurations
        ArrayList<Configure> configurations = new ArrayList<Configure>();
        //An arrayList to store all scanActors
        ArrayList<ScanActor> scanActors = new ArrayList<ScanActor>();

        //Create the collection Actor
        CollectionActor collectionActor = new CollectionActor();

        //Configuration Object to be constructed for every Scanactor
        Configure configuration;


        //Loop through each file and queue up the tasks to the thread pool
        for(String file: this.files){

            //NOTE: not sure if the last parameter is correct
            //Create new configuration and queue it within the list
            configurations.add(new Configure(file, this.searchPattern, collectionActor.context()));
            scanActors.add(new ScanActor());
        }

        //Create FileCount object
        FileCount fileCount = new FileCount(this.files.length);

        //send fileCount to collection actor

        //send configuations to scan actors
        for(Configure config: configurations){

        }

        //Scan actor will send a found message to collection actor

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
