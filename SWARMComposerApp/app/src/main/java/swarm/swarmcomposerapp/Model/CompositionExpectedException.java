package swarm.swarmcomposerapp.Model;

/**
 * A new type of RuntimeException that should be thrown if one expects
 * that something should be an instance of Composition.
 */
public class CompositionExpectedException extends RuntimeException {

    /**
     * A basic form of the exception displaying a standard text.
     */
    public CompositionExpectedException() {
        super("Something that should be a Composition object turned out to be something else.");
    }

    /**
     * A CompositionExpectedException with a defined message.
     * @param msg
     */
    public CompositionExpectedException(String msg) {
        super(msg);
    }
}
