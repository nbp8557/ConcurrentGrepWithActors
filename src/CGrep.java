import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import akka.actor.ActorRef;
import akka.actor.ActorRegistry;

import static akka.actor.Actors.*;

public class CGrep{
    //String array to store file names temporarily
    private String[] files;
    //String to store the regex search pattern temporarily
    private String searchPattern;

    public CGrep(String[] files, String searchPattern){
        this.files = files;
        this.searchPattern = searchPattern;
    }

    //Search all of the files
    public void SearchAllFiles(){
        ActorRegistry registry = new ActorRegistry();
        //An arrayList to store all configurations
        ArrayList<Configure> configurations = new ArrayList<Configure>();
        //An arrayList to store all scanActors
        ArrayList<ActorRef> scanActors = new ArrayList<ActorRef>();

        //Create the collection Actor
        ActorRef collectionActor = actorOf(CollectionActor.class);

        //Configuration Object to be constructed for every Scanactor
        Configure configuration;


        //Loop through each file and queue up the tasks to the thread pool
        for(String file: this.files){


            //Doing the check to prevent a bug

            //Create new configuration and queue it within the list
            if(file != null){
                configurations.add(new Configure(file, this.searchPattern, collectionActor));
                scanActors.add(actorOf(ScanActor.class));
            }

        }

        //Create FileCount object
        FileCount fileCount = new FileCount(this.files.length);

        //Start actors
        collectionActor.start();
        registry.register(collectionActor);

        for(ActorRef scan: scanActors){
            scan.start();
            registry.register(scan);
        }


        //send fileCount to collection actor
        collectionActor.tell(fileCount);

        int counter=0;
        //send configuations to scan actors
        for(Configure config: configurations){
            scanActors.get(counter).tell(config);
            counter++;
        }

        //Scan actor will send a found message to collection actor
        registry.shutdownAll();
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
        CGrep grep = new CGrep(file,searchPattern);

        //Search all of the files
        grep.SearchAllFiles();
    }


}
