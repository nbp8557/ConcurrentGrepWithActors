import akka.actor.Actors;
import akka.actor.UntypedActor;

/**
 * Created by Brooker on 11/11/2015.
 */
public class CollectionActor extends UntypedActor {

    public FileCount fileCount;

    @Override
    //Can recieve two types of Messages
    // FileCount - contains a # of files being scanned
    // Found - Objects that are the results of the Collection Actor
    public void onReceive(Object message){

        //If the message is a FileCount object
        if(message.getClass().getName() == "FileCount"){
            //save the FileCount object to the CollectionActor
            this.fileCount = (FileCount) message;
        }
        //If the message is a Found object and
        // the number of files that have not yet been read is greater than 0
        else if((message.getClass().getName() == "Found") && (this.fileCount.fileCount > 0)  ){

            //Cast the object
            Found finalResult = (Found) message;

            if(finalResult.results.size()>0) {
                System.out.println("\nThe results for the File: " + finalResult.filename);
                //Print each result's line number and text
                for (String line : finalResult.results) {
                    System.out.println(line);
                }
            }

            this.fileCount.fileCount--;
        }

        //All files have been read, now shutdown all actors
        if(this.fileCount.fileCount <= 0){
            Actors.registry().shutdownAll();
        }

    }

}
