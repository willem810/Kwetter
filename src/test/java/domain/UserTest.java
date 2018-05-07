package domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class UserTest {
    @Test
    public void followUser() {
        User u1 = new User();
        User u2 = new User();

        Assert.assertEquals(new ArrayList<User>(), u1.getFollowing());
        Assert.assertEquals(new ArrayList<User>(), u2.getFollowers());

        u1.followUser(u2);

        ArrayList<User> expectedFollowing = new ArrayList<>();
        expectedFollowing.add(u2);

        ArrayList<User> expectedFollowers = new ArrayList<>();
        expectedFollowers.add(u1);

        Assert.assertEquals("User 1 does not follow the expected users", expectedFollowing, u1.getFollowing());
        Assert.assertEquals("User 2 doesn't have the expected followers", expectedFollowers, u2.getFollowers());
    }
}
