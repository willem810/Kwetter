package exception;

public class TweetNotFoundException extends Exception {
    public TweetNotFoundException() {
        super("Tweet not found!");
    }

    public TweetNotFoundException(String name) {
        super("Tweet with name: "+name+" not found!");
    }

    public TweetNotFoundException(Long id) {
        super("Tweet with id: "+id+" not found!");
    }
}
