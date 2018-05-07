package socket;

import domain.Tweet;
import domain.User;
import socket.encoder.tweet.TweetEncoder;

import javax.ejb.Stateless;
import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint(
        value = "/socket/tweet",
        encoders = {TweetEncoder.class},
        configurator = Configurator.class
)
@Stateless
public class TweetEndpoint {
    public TweetEndpoint() {
        System.out.println("class loaded " + this.getClass());
    }

    //
//    static Queue<Session> queue = new ConcurrentLinkedQueue<>();
    private static HashMap<Session, String> sessions = new HashMap<>();

    public void sendTweet(Tweet tweet) {
        try {
            List<String> usersToPush = new ArrayList<>();
            usersToPush.add(tweet.getUser().getUsername());

            for (User u : tweet.getUser().getFollowers()) {
                usersToPush.add(u.getUsername());
            }

            /* Send updates to all open WebSocket sessions */
            for (Map.Entry<Session, String> ent : sessions.entrySet()) {
                String username = ent.getValue();

                if (usersToPush.contains(username)) {
                    Session session = ent.getKey();
                    session.getBasicRemote().sendObject(tweet);
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        } catch (EncodeException e) {
            System.err.println(e.toString());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (message.startsWith("sub:")) {
            String username = message.replace("sub:", "");
            sessions.put(session, username);
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        System.out.printf("Session opened, id: %s%n", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable e) {
        sessions.remove(session);
        e.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.printf("Session closed with id: %s%n", session.getId());
    }
}
