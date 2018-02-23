package domain;

import java.util.Date;

public class Tweet {
    private Long id;
    private User user;
    private String message;
    private Date datePlaced;
    private int likes;

    public Tweet() {}

    public Tweet(User user, String message) {
        this.user = user;
        this.message = message;
        this.datePlaced = new Date();
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

    public Date getDatePlaced() {
        return datePlaced;
    }

    public void setDatePlaced(Date datePlaced) {
        this.datePlaced = datePlaced;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
