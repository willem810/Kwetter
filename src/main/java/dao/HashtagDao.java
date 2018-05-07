package dao;

import domain.Hashtag;

import java.util.ArrayList;

public interface HashtagDao {

    void addHashtag(Hashtag hashtag);
    void removeHashtag(Hashtag hashtag);
    ArrayList<Hashtag> getHashtags();
    Hashtag findByName(String name);
}
