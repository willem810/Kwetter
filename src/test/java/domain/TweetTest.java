package domain;

import org.junit.Assert;
import org.junit.Test;

public class TweetTest {
    @Test
    public void like() {
        Tweet t = new Tweet();
        Assert.assertEquals(0, t.getLikes());

        t.like();
        Assert.assertEquals(1, t.getLikes());

        t.unlike();
        Assert.assertEquals(0, t.getLikes());
    }
}
