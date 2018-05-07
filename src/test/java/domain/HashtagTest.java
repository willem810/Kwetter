package domain;

import org.junit.Assert;
import org.junit.Test;

import javax.json.JsonObject;

public class HashtagTest {

    @Test
    public void toJson() {
        Hashtag tag = new Hashtag();
        tag.setId(11L);
        tag.setName("testTag");

        JsonObject obj = tag.toJson();

        Assert.assertEquals(tag.getId().intValue(), obj.getInt("id"));
        Assert.assertEquals(tag.getName(), obj.getString("name"));
    }
}
