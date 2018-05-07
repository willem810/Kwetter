package rest;

import domain.Hashtag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import service.HashtagService;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HashtagResourceTest {
    HashtagResource hResource = new HashtagResource();

    @Mock
    HashtagService hService;


    @Before
    public void setUp() {
        hResource.setHashtagService(hService);
    }

    @Test
    public void addHashtag() {
        Hashtag tag = new Hashtag("testtag");

        Response resp = hResource.addHashtag(tag);

        Assert.assertEquals(200, resp.getStatus());
        verify(hService, times(1)).addHashtag(tag);
    }

    @Test
    public void removeHashtag() {
        Hashtag tag = new Hashtag("testtag");
        when(hService.findByName(tag.getName())).thenReturn(tag);

        Response resp = hResource.removeHashtag(tag.getName());

        Assert.assertEquals(200, resp.getStatus());
        verify(hService, times(1)).removeHashtag(tag);
    }

    @Test
    public void getHashtags() {
        ArrayList<Hashtag> expectedList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Hashtag tag = new Hashtag("testTag" + i);
            tag.setId(new Long(i));
            expectedList.add(tag);
        }

        when(hService.getHashtags()).thenReturn(expectedList);

        Response resp = hResource.getHashtags();

        Assert.assertEquals(200, resp.getStatus());
        JsonArray list = (JsonArray) resp.getEntity();

        for (int i = 0; i < expectedList.size(); i++) {
            Assert.assertEquals(expectedList.get(i).toJson(), list.getJsonObject(i));
        }
    }

    @Test
    public void findByName() {
        Hashtag tag = new Hashtag("testtag");
        tag.setId(1L);
        when(hService.findByName(tag.getName())).thenReturn(tag);

        Response resp = hResource.findByName(tag.getName());
        JsonObject actualObj = (JsonObject) resp.getEntity();

        Assert.assertEquals(200, resp.getStatus());
        Assert.assertEquals(tag.getName(), actualObj.getString("name"));

    }
}