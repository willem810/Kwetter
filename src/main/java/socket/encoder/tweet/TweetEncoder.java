package socket.encoder.tweet;

import domain.Tweet;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.nio.ByteBuffer;

public class TweetEncoder implements Encoder.Text<Tweet>{



    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(Tweet object) throws EncodeException {
        return object.toJson().toString();
    }
}
