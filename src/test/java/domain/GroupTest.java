package domain;

import org.junit.Assert;
import org.junit.Test;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GroupTest {


    @Test
    public void addPermission() {
        ArrayList<Permissions> expected = new ArrayList<>();
        Group g = new Group("testGroup", expected);

        List<Permissions> actual = g.getPermissions();
        Assert.assertEquals("initialization of Group failed, list is not empty", expected.size(), actual.size());

        g.addPermission(Permissions.user_basic);
        expected.add(Permissions.user_basic);

        Assert.assertEquals("Lists not equal", expected, actual);


    }

    @Test
    public void removePermission() {
        ArrayList<Permissions> expected = new ArrayList<>();
        expected.add(Permissions.user_basic);
        expected.add(Permissions.tweet_basic);

        Group g = new Group("testGroup", expected);

        List<Permissions> actual = g.getPermissions();
        Assert.assertEquals("initialization of Group failed, list is not of same size", expected.size(), actual.size());

        g.removePermission(Permissions.user_basic);
        expected.remove(Permissions.tweet_basic);

        Assert.assertEquals("Lists not equal", expected, actual);
    }

    @Test
    public void toJson() {
        Group g = new Group();
        g.setId(12L);
        g.setName("testGroup");
        g.addPermission(Permissions.user_basic);
        g.addPermission(Permissions.tweet_basic);

        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("user_basic");
        permissions.add("tweet_basic");

        JsonObject obj = g.toJson();
        Assert.assertEquals(g.getName(), obj.getString("name"));
        Assert.assertEquals(g.getId().intValue(), obj.getInt("id"));

        JsonArray actual = obj.getJsonArray("permissions");
        for (int i = 0; i < permissions.size(); i++ ){
            assertEquals(permissions.get(i), actual.getString(i));
        }
    }
}
