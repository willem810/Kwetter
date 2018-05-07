package dao.coll;

import dao.Collection.UserDaoColl;
import dao.UserDao;
import domain.User;
import exception.UserNotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserDaoCollTest {
    private UserDao userDao;

    @Before
    public void setUp() {
        userDao = new UserDaoColl();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void findByUsername() throws UserNotFoundException {
        userDao.addUser(new User("Willem", "Willem Toemen", "test", "test"));
        userDao.addUser(new User("Ken", "Ken van Grinsven", "test", "test"));
        userDao.addUser(new User("Lorenzo", "Lorenzo van den Weerden", "test", "test"));
        userDao.addUser(new User("Youri", "Youri Schuurmans", "test", "test"));

        Assert.assertEquals("Users not added correctly", 4, userDao.getUsers().size());

        User willem = userDao.findByUsername("Willem");
        User ken = userDao.findByUsername("Ken");
        User lorenzo = userDao.findByUsername("Lorenzo");
        User youri = userDao.findByUsername("Youri");

        Assert.assertEquals("Willem Toemen", willem.getName());
        Assert.assertEquals("Ken van Grinsven", ken.getName());
        Assert.assertEquals("Lorenzo van den Weerden", lorenzo.getName());
        Assert.assertEquals("Youri Schuurmans", youri.getName());
    }

    @Test (expected = UserNotFoundException.class)
    public void findByUsernameUserNotFoundException() throws UserNotFoundException {
        User fakeUser = userDao.findByUsername("fakeUser");
    }
}
