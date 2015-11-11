import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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


        //Loop through each file and queue up the tasks to the thread pool
        for(String file: this.files){
            //Make sure that the file is not null
            if(file != null) {
                //Create a new SearchTask for each file which will search for the regex
                // and submit it to the ThreadPool for execution.
                //Also add the Future Object result from the Task to the results file
                results.add(this.threadResults.submit(new SearchTask(file,this.searchPattern)));

            }
        }

        //Iterate through all of the results
        // we have to loop a specific number of times because the ExecutorCompletionService
        // Does not know how many times it calls the take method
        for(int counter = 0; counter < results.size(); counter++) {
            final Future<Found> threadResult;

            try {
                //Get the future object when it has completed execution
                threadResult = this.threadResults.take();
                try {
                    //Get the return value from the future AKA the Result Object
                    final Found finalResult = threadResult.get();

                    //If the file contained at least one match output the results
                    if(finalResult.results.size()>0) {
                        System.out.println("\nThe results for the File: " + finalResult.filename);
                        //Print each result's line number and text
                        for (String line : finalResult.results) {
                            System.out.println(line);
                        }
                    }
		    else{
                        System.out.println("\nThere was no results for the File: " + finalResult.filename );
		    }

                } catch (ExecutionException e) {
                    System.out.println("EXECUTION ERROR");
                    System.out.println("Future threw an exception while running");

                    //FOR DEBUGING
                    //e.printStackTrace();
                }
            } catch (InterruptedException e) {
                //Issue when taking the results from the ExecutorCompletionService
                e.printStackTrace();
            }

        }

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
