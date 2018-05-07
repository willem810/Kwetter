package service;

import dao.HashtagDao;
import dao.JPA.JPA;
import domain.Hashtag;
import interceptor.Logging;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
@Logging
public class HashtagService {

    @Inject @JPA
    HashtagDao hashtagDao;

    public void setHashtagDao(HashtagDao dao) {
        this.hashtagDao = dao;
    }

    /**
     * addHashtag adds a hashtag to the persistence unit
     * @param hashtag the hashtag to add
     */
    public void addHashtag(Hashtag hashtag){
        hashtagDao.addHashtag(hashtag);
    }

    /**
     * removeHashtag removes a hashtag in the persistence unit
     * @param hashtag the hashtag to remove
     */
    public void removeHashtag(Hashtag hashtag){
        hashtagDao.removeHashtag(hashtag);
    }

    /**
     * getHashtags returns all hashtags
     * @return returns all hashtags
     */
    public ArrayList<Hashtag> getHashtags(){
        return hashtagDao.getHashtags();
    }

    /**
     * findByName returns a Hashtag with a given name.
     * @param name the name of the hashtag
     * @return a hashtag
     */
    public Hashtag findByName(String name) {
        return hashtagDao.findByName(name);
    }

}
