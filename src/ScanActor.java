/**
 * Created by Brooker on 11/11/2015.
 */
import akka.*;
import akka.actor.UntypedActor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ScanActor extends UntypedActor {
    public Configure configuration;

    @Override
    public void onReceive(Object message){

        //if the Object sent is the configure class
        if(message.getClass().getName() == Configure.class.getName()){

            //set the ScanActors configuration object
            this.configuration = (Configure) message;

            //counter to keep track of the file line
            int lineCounter = 0;
            //Used to store each line in the file to perform the regex search on
            String lineText;

            ArrayList<String> results = new ArrayList<String>();

            //Use a BufferedReader to read each line in the file
            try {
                BufferedReader reader = new BufferedReader(new FileReader(this.configuration.file));
                while ((lineText = reader.readLine()) != null) {

                    //Append the line of the file
                    lineCounter++;

                    //if the line of the file matches the regex
                    if (Pattern.matches(this.configuration.searchRegex, lineText)) {
                        //Add the line number and the lines contents to the results list
                        results.add(lineCounter + ": " + lineText);
                    }
                }
            } catch (IOException e) {
                //Something IO related messed up
                System.out.println("INVALID FILENAME");
                System.out.println("The Filename: '" + this.configuration.file + "' Does not exist.");

                //FOR DEBUGING
                //e.printStackTrace();
            }

            //construct the result object and send to the CollectionActor
            this.configuration.collectionActorReference.tell(new Found(this.configuration.file,results));
        }



        /*
Each file (or the standard input if no files are given) will be scanned
by a ScanActor for occurrences of the pattern, using one actor per file.
 A ScanActor expects to receive exactly one immutable Configure message
 containing (a) a String with the name of the file to scan (or null for
 the standard input), and (b) an ActorRef to a CollectionActor, which
 collects and prints scan results. The actual format of the Configure
 objects is up to you.

Each ScanActor will construct an immutable Found object containing
a String with the name of the file, and a List<String> with one entry
in the list for each matching line. Each string in the list consists of t
he line number, a space, and the text of the line itself. The list must
 be ordered by location of the line in the file (i.e., first matching line
  at position 0, second matching line at position 1, etc.). The Found object
  is sent as the one and only message from the ScanActor to the CollectionActor.

         */
    }

}
