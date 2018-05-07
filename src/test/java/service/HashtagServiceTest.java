package service;

import dao.HashtagDao;
import domain.Hashtag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HashtagServiceTest {

    HashtagService hService;

    @Mock
    HashtagDao hDao;

    @Before
    public void setUp(){
        hService = new HashtagService();
        hService.setHashtagDao(hDao);
    }

    @Test
    public void addHashtag() {
        Hashtag t1 = new Hashtag("tag1");
        Hashtag t2 = new Hashtag("tag2");

        hService.addHashtag(t1);
        hService.addHashtag(t2);

        verify(hDao, times(1)).addHashtag(t1);
        verify(hDao, times(1)).addHashtag(t2);
    }

    @Test
    public void removeHashtag() {
        Hashtag t1 = new Hashtag("tag1");
        Hashtag t2 = new Hashtag("tag2");

        hService.removeHashtag(t1);
        hService.removeHashtag(t2);

        verify(hDao, times(1)).removeHashtag(t1);
        verify(hDao, times(1)).removeHashtag(t2);
    }

    @Test
    public void getHashtags() {

        Hashtag t1 = new Hashtag("tag1");
        Hashtag t2 = new Hashtag("tag2");

        ArrayList<Hashtag> expected = new ArrayList<>();
        expected.add(t1);
        expected.add(t2);

        when(hDao.getHashtags()).thenReturn(expected);

        Assert.assertEquals(expected, hService.getHashtags());
    }

    @Test
    public void findByName() {

        Hashtag t1 = new Hashtag("tag1");
        Hashtag t2 = new Hashtag("tag2");

        when(hDao.findByName(t1.getName())).thenReturn(t1);
        when(hDao.findByName(t2.getName())).thenReturn(t2);

        Hashtag t1Found = hService.findByName(t1.getName());
        Hashtag t2Found = hService.findByName(t2.getName());

        Assert.assertEquals(t1, t1Found);
        Assert.assertEquals(t2, t2Found);
    }
}