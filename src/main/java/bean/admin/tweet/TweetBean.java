package bean.admin.tweet;

import domain.Hashtag;
import domain.Tweet;
import service.TweetService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named("tweetBean")
@RequestScoped
public class TweetBean {
    @Inject
    private TweetService tService;
    private String filter;

    public List<Tweet> getTweets() {
        List<Tweet> tweets = tService.getTweets();
        List<Tweet> returnList = new ArrayList<>();
        if (filter != null && filter.length() > 0) {
            for (Tweet t : tweets) {
                if (t.getUser().getUsername().toLowerCase().contains(filter)) {
                    returnList.add(t);
                }

                for (Hashtag tag : t.getHashtags()) {
                    if (tag.getName().toLowerCase().contains(filter)) {
                        returnList.add(t);
                    }
                }
            }
        } else{
            returnList = tweets;
        }

        Collections.sort(returnList, Comparator.comparing(Tweet::getDatePlaced));
        Collections.reverse(returnList);

        return returnList;
    }

    public void removeUser(Tweet tweet) {tService.removeTweet(tweet);}

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter.toLowerCase();
    }

    public String goToAdmin() {
        return "../index.xhtml";
    }
}
