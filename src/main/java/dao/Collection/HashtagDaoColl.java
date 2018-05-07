package dao.Collection;

import dao.HashtagDao;
import domain.Hashtag;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.ArrayList;

@Stateless
@Default
public class HashtagDaoColl implements HashtagDao {

    ArrayList<Hashtag> hashtags = new ArrayList<>();

    @Override
    public void addHashtag(Hashtag hashtag) {
        hashtags.add(hashtag);
    }

    @Override
    public void removeHashtag(Hashtag hashtag) {
        hashtags.remove(hashtag);
    }

    @Override
    public ArrayList<Hashtag> getHashtags() {
        return hashtags;
    }

    @Override
    public Hashtag findByName(String name) {
        for (Hashtag h : hashtags) {
            if (h.getName().equals(name)) {
                return h;
            }
        }

        Hashtag newTag = new Hashtag(name);
        addHashtag(newTag);

        return newTag;
    }
}
