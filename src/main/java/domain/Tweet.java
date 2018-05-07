package domain;

import javax.json.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "tweet.findById", query = "SELECT t FROM Tweet t WHERE t.id = :id"),
        @NamedQuery(name = "tweet.findByUsername", query = "SELECT t FROM Tweet t WHERE t.user.username = :username ORDER BY t.datePlaced DESC, t.id DESC"),
        @NamedQuery(name = "tweet.findRecentTweets", query = "SELECT t FROM Tweet t WHERE t.user.username = :username ORDER BY t.datePlaced DESC, t.id DESC"),
        @NamedQuery(name = "tweet.getTweetCount", query = "SELECT COUNT(t) FROM Tweet t WHERE t.user.username = :username")

})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "tweet.getFeed",
                query = "SELECT * FROM kwetter.tweet WHERE (USER IN ( SELECT username FROM kwetter.user WHERE ID IN ( SELECT FOLLOWING_ID FROM kwetter.user_following WHERE USER_ID = ?))) OR (USER IN (SELECT username FROM kwetter.user WHERE id = ?)) ORDER BY datePlaced DESC, id DESC",
                resultClass = Tweet.class)
})
public class Tweet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "USER", referencedColumnName = "username")
    private User user;
    private String message;
    private long datePlaced;
    private int likes;

    @ManyToMany
    @JoinTable(
            name = "TWEET_HASHTAG",
            joinColumns = @JoinColumn(name = "TWEET_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "HASHTAG_ID", referencedColumnName = "id"))
    private List<Hashtag> hashtags = new ArrayList<>();

    public Tweet() {
    }

    public Tweet(User user, String message) {
        this.id = -1L;
        this.likes = 0;
        this.user = user;
        this.message = message;
        this.datePlaced = System.currentTimeMillis();
    }

    public Tweet(Long id, User user, String message) {
        this.id = id;
        this.likes = 0;
        this.user = user;
        this.message = message;
        this.datePlaced = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDatePlaced() {
        return datePlaced;
    }

    public void setDatePlaced(long datePlaced) {
        this.datePlaced = datePlaced;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void like() {
        this.likes++;
    }

    public void unlike() {
        this.likes--;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(ArrayList<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public JsonObject toJson() {
        JsonObjectBuilder objBuilder = Json.createObjectBuilder().
                add("id", id).
                add("user", user.getUsername()).
                add("message", message).
                add("datePlaced", datePlaced).
                add("likes", likes);

        JsonArrayBuilder hashtagBuilder = Json.createArrayBuilder();
        if (hashtags.size() > 0) {
            for (Hashtag tag : hashtags) {
                hashtagBuilder.add(tag.getName());
            }
        }
        objBuilder.add("hashtags", hashtagBuilder);

        JsonObjectBuilder relBuilder = Json.createObjectBuilder()
                .add("remove", "/resources/tweets/remove"  + this.getId())
                .add("like", "/resources/tweets/"  + this.getId() + "/like")
                .add("unlike", "/resources/tweets/"  + this.getId() + "/unlike");

        objBuilder.add("rel", relBuilder);

        return objBuilder.build();
    }
}
