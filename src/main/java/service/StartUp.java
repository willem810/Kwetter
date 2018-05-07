package service;

import domain.*;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ArrayList;

@Singleton
@Startup
public class StartUp {
    @Inject
    private UserService userService;

    @Inject
    private TweetService tweetService;

    @Inject
    private HashtagService hashtagService;

    @Inject
    private GroupService groupService;

    public StartUp() {
    }

    @PostConstruct
    private void intData() {

        User willem = new User("Willem", "pass", "Willem Toemen", "www.willem.nl", "Hello, I am willem", new ArrayList<>());
        User ken = new User("Ken","pass", "Ken van Grinsven", "www.ken.nl", "Hello, I am ken", new ArrayList<>());
        User lorenzo = new User("Lorenzo","pass", "Lorenzo van den Weerden", "www.Lorenzo.nl", "Hello, I am lorenzo", new ArrayList<>());
        User youri = new User("Youri","pass", "Youri Schuurmans", "www.youri.nl", "Hello, I am youri", new ArrayList<>());
        User henk = new User("Henk","pass", "Henk Henkert", "www.henk.nl", "Hello, I am henk", new ArrayList<>());
        User piet = new User("Piet","pass", "Piet Pietert", "www.piet.nl", "Hello, I am piet", new ArrayList<>());
        User jan = new User("Jan","pass", "Jan Jannert", "www.jan.nl", "Hello, I am jan", new ArrayList<>());
        User josh = new User("Josh","pass", "Josh Joshert", "www.josh.nl", "Hello, I am josh", new ArrayList<>());
        User drake = new User("Drake","pass", "Drake Drakert", "www.drake.nl", "Hello, I am drake", new ArrayList<>());
        User joris = new User("Joris","pass", "Joris Jorisert", "www.joris.nl", "Hello, I am joris", new ArrayList<>());

        userService.addUser(willem);
        userService.addUser(ken);
        userService.addUser(lorenzo);
        userService.addUser(youri);
        userService.addUser(henk);
        userService.addUser(piet);
        userService.addUser(jan);
        userService.addUser(josh);
        userService.addUser(drake);
        userService.addUser(joris);

        userService.followUser(willem, ken);
        userService.followUser(willem, lorenzo);
        userService.followUser(willem, youri);
        userService.followUser(ken, willem);
        userService.followUser(lorenzo, willem);
        userService.followUser(youri, ken);
        userService.followUser(lorenzo, ken);
        userService.followUser(lorenzo, joris);
        userService.followUser(joris, lorenzo);
        userService.followUser(drake, youri);

        Group guestGroup = new Group("Guest", new ArrayList() {{
            add(Permissions.hashtag_guest);
            add(Permissions.group_guest);
            add(Permissions.tweet_guest);
            add(Permissions.user_guest);
        }});

        Group basicGroup = new Group("Basic", new ArrayList() {{
            add(Permissions.hashtag_basic);
            add(Permissions.group_basic);
            add(Permissions.tweet_basic);
            add(Permissions.user_basic);
        }}, new ArrayList() {{
            add(guestGroup);
        }});

        Group moderatorGroup = new Group("Moderator", new ArrayList() {{
            add(Permissions.hashtag_moderator);
            add(Permissions.group_moderator);
            add(Permissions.tweet_moderator);
            add(Permissions.user_moderator);
        }}, new ArrayList() {{
            add(basicGroup);
        }});

        Group adminGroup = new Group("Admin", new ArrayList() {{
            add(Permissions.hashtag_admin);
            add(Permissions.group_admin);
            add(Permissions.tweet_admin);
            add(Permissions.user_admin);
        }}, new ArrayList() {{
            add(moderatorGroup);
        }});

        groupService.addGroup(guestGroup);
        groupService.addGroup(basicGroup);
        groupService.addGroup(moderatorGroup);
        groupService.addGroup(adminGroup);

        userService.addGroup(willem, adminGroup);
        userService.addGroup(ken, moderatorGroup);
        userService.addGroup(lorenzo, moderatorGroup);
        userService.addGroup(youri, moderatorGroup);
        userService.addGroup(henk, basicGroup);
        userService.addGroup(piet, basicGroup);
        userService.addGroup(jan, basicGroup);
        userService.addGroup(josh, basicGroup);
        userService.addGroup(drake, basicGroup);
        userService.addGroup(joris, basicGroup);

        tweetService.addTweet(new Tweet(willem, "Willems #first #tweet"));
        tweetService.addTweet(new Tweet(willem, "Willems second tweet"));
        tweetService.addTweet(new Tweet(willem, "Willems third tweet @Drake"));
        tweetService.addTweet(new Tweet(ken, "Ken first tweet @Jan"));
        tweetService.addTweet(new Tweet(ken, "Ken #second tweet"));
        tweetService.addTweet(new Tweet(lorenzo, "Lorenzo first tweet"));
        tweetService.addTweet(new Tweet(youri, "Youri first tweet @Willem"));
        tweetService.addTweet(new Tweet(youri, "Youri second #tweet"));
        tweetService.addTweet(new Tweet(youri, "Youri #third tweet @Joris"));
        tweetService.addTweet(new Tweet(youri, "Youri fourth tweet"));

        hashtagService.addHashtag(new Hashtag("Kwetter"));
        hashtagService.addHashtag(new Hashtag("Twitter"));
        hashtagService.addHashtag(new Hashtag("Trump"));
        hashtagService.addHashtag(new Hashtag("JEA"));
        hashtagService.addHashtag(new Hashtag("SOP"));
        hashtagService.addHashtag(new Hashtag("ICT"));
        hashtagService.addHashtag(new Hashtag("Software"));
        hashtagService.addHashtag(new Hashtag("JavaEE"));
        hashtagService.addHashtag(new Hashtag("ML"));
        hashtagService.addHashtag(new Hashtag("Docker"));
    }
}
