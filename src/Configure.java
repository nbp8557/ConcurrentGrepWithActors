import akka.actor.ActorRef;

/**
 Stores the information needed to scan a single file
 */
public class Configure {
    public final String file;
    //Stores the search regex
    public final String searchRegex;
    //Stores reference to CollectionActor
    public final ActorRef collectionActorReference;

    public Configure(String file, String searchRegex,ActorRef collectionActorReference) {
        this.file = file;
        this.searchRegex = searchRegex;
        this.collectionActorReference = collectionActorReference;
    }

    public String getFile(){ return file }

    public ActorRef getCollectionActorReference() { return collectionActorReference }
}
