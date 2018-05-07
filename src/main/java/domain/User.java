package domain;

import util.PasswordHash;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.PERSIST;

@Entity
@NamedQueries({
        @NamedQuery(name = "user.findByName", query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "user.searchByName", query = "SELECT u FROM User u WHERE u.username LIKE :query"),
        @NamedQuery(name = "user.getPassword", query = "SELECT u.password FROM User u WHERE u.username = :username")
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String location;
    private String web;
    private String bio;

    //    @ManyToMany( cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE
//    })
    @JoinTable(
            name = "USER_GROUPS",
            joinColumns = @JoinColumn(name = "USERNAME", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "GROUPNAME", referencedColumnName = "name"))
    @OneToMany
    public List<Group> Groups = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "USER_FOLLOWING",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "FOLLOWING_ID", referencedColumnName = "id"))
    public List<User> Following = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "USER_FOLLOWER",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "FOLLOWER_ID", referencedColumnName = "id"))
    public List<User> Followers = new ArrayList<>();

    public User() {
    }


    public User(String username, String name, String web, String bio) {
        this.username = username;
        this.name = name;
        this.web = web;
        this.bio = bio;
        this.location = "";
        setPassword("DEFAULT PASSWORD, PLEASE CHANGE");
    }

    public User(String username, String password, String name) {
        this.username = username;
        setPassword(password);
        this.name = name;
    }

    public User(String username, String password, String name, String web, String bio, List<Group> groups) {
        this.username = username;
        setPassword(password);
        this.name = name;
        this.web = web;
        this.bio = bio;
        this.location = "";
        this.Groups = groups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = PasswordHash.stringToHash(password);
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Group> getGroups() {
        return Groups;
    }

    public void addGroup(Group group) {
        Groups.add(group);
    }

    public void removeGroup(Group group) {
        Groups.remove(group);
    }

    public List<User> getFollowing() {
        return Following;
    }

    public void followUser(User u) {
        if (!Following.contains(u)) {
            Following.add(u);
        }
        u.addFollower(this);
    }

    public void unFollowUser(User u) {
        Following.remove(u);
        u.removeFollower(this);
    }

    public List<User> getFollowers() {
        return Followers;
    }

    public void addFollower(User u) {
        if (!Followers.contains(u)) {
            Followers.add(u);
        }
    }

    public void removeFollower(User u) {
        Followers.remove(u);
    }

    public JsonObject toJson() {
        if (id == null) this.id = (long) -1;
        JsonObjectBuilder objBuilder = Json.createObjectBuilder().
                add("id", id).
                add("name", name).
                add("username", username).
                add("location", location).
                add("web", web).
                add("bio", bio);

        JsonArrayBuilder followersBuilder = Json.createArrayBuilder();
        if (Followers.size() > 0) {
            for (User follower : Followers) {
                followersBuilder.add(follower.getUsername());
            }
        }
        objBuilder.add("followers", followersBuilder);

        JsonArrayBuilder followingBuilder = Json.createArrayBuilder();
        if (Following.size() > 0) {
            for (User following : Following) {
                followingBuilder.add(following.getUsername());
            }
        }
        objBuilder.add("following", followingBuilder);

        JsonArrayBuilder groupsBuilder = Json.createArrayBuilder();
        if (Groups.size() > 0) {
            for (Group group : Groups) {
                groupsBuilder.add(group.getName());
            }
        }
        objBuilder.add("groups", groupsBuilder);


        JsonObjectBuilder relBuilder = Json.createObjectBuilder()
                .add("tweets", "/resources/users/"  + this.getUsername() + "/tweets/")
                .add("tweetCount", "/resources/users/"  + this.getUsername() + "/tweets/count")
                .add("tweet", "/resources/users/"  + this.getUsername() + "/tweet")
                .add("followUser", "/resources/users/" + this.getUsername() + "/follow?u=")
                .add("unFollowUser", "/resources/users/" + this.getUsername() + "/unFollow?u=")
                .add("addGroup", "/resources/users/" + this.getUsername() + "/addGroup?group=")
                .add("removeGroup", "/resources/users/" + this.getUsername() + "/removeGroup?group=")
                .add("feed", "/resources/users/" + this.getUsername() + "/feed");

        objBuilder.add("rel", relBuilder);

        return objBuilder.build();
    }
}
