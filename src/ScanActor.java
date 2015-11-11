/**
 * Created by Brooker on 11/11/2015.
 */
import akka.*;
import akka.actor.UntypedActor;

public class ScanActor extends UntypedActor {

    @Override
    public void onReceive(Object message){
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
