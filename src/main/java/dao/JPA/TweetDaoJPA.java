package dao.JPA;

import dao.TweetDao;
import domain.Tweet;
import domain.User;
import exception.TweetNotFoundException;
import exception.UserNotFoundException;
import interceptor.Managed;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Stateless
@JPA
public class TweetDaoJPA implements TweetDao {
    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;

    public TweetDaoJPA() {
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }


    @Override
    public Tweet addTweet(Tweet tweet) {
        em.persist(tweet);
        em.flush();
//        tweet = em.merge(tweet);
        return tweet;
    }

    @Override
    @Managed
    public void removeTweet(Tweet tweet) {
        em.remove(tweet);
    }

    @Override
    public List<Tweet> getTweets() {
        Query query = em.createQuery("SELECT t FROM Tweet t");
        return new ArrayList<>(query.getResultList());
    }

    @Override
    public List<Tweet> getRecentTweets(String username, int count) throws UserNotFoundException {
        TypedQuery<Tweet> query = em.createNamedQuery("tweet.findRecentTweets", Tweet.class);
        query.setParameter("username", username);
        query.setMaxResults(count);
        List<Tweet> result = query.getResultList();
        if (result.size() <= 0) {
            throw new UserNotFoundException(username);
        }

        return result;
    }

    @Override
    public Tweet findTweetById(Long id) throws TweetNotFoundException {
        TypedQuery<Tweet> query = em.createNamedQuery("tweet.findById", Tweet.class);
        query.setParameter("id", id);
        List<Tweet> result = query.getResultList();
        if (result.size() <= 0) {
            throw new TweetNotFoundException(id);
        }

        return result.get(0);
    }

    @Override
    public List<Tweet> findTweetsByUsername(String name) throws UserNotFoundException {
        TypedQuery<Tweet> query = em.createNamedQuery("tweet.findByUsername", Tweet.class);
        query.setParameter("username", name);
        List<Tweet> result = query.getResultList();
        if (result.size() <= 0) {
            throw new UserNotFoundException(name);
        }

        return result;
    }

    @Override
    public long getTweetCount(String username) throws UserNotFoundException {
        TypedQuery<Long> query = em.createNamedQuery("tweet.getTweetCount", Long.class);
        query.setParameter("username", username);
        List<Long> result = query.getResultList();
        if (result.size() <= 0) {
            throw new UserNotFoundException(username);
        }

        return result.get(0);
    }

    @Override
    public List<Tweet> getFeed(String username) throws UserNotFoundException {

        String nativeQueryGetId = "SELECT id FROM kwetter.user WHERE USERNAME = '" + username + "';";
        Query idQuery = em.createNativeQuery(nativeQueryGetId);
        List<Long> ids = idQuery.getResultList();
        if (ids.size() <= 0) {
            throw new UserNotFoundException(username);
        }

        long id = ids.get(0);

        TypedQuery<Tweet> query = em.createNamedQuery("tweet.getFeed", Tweet.class);
        query.setParameter(1, id);
        query.setParameter(2, id);
        query.setMaxResults(10);
        List<Tweet> tweets = query.getResultList();


        return tweets;
    }

    @Override
    @Managed
    public void Like(Tweet tweet) {
        tweet.like();

    }

    @Override
    @Managed
    public void Unlike(Tweet tweet) {
        tweet.unlike();

    }
}
