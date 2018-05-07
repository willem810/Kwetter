package dao.JPA;

import dao.UserDao;
import domain.User;
import exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import service.TweetService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoJPATest {

    private UserDao userDao;

    @Mock
    private EntityManager em;

    @Mock
    private TweetService tService;

    @Before
    public void setUp() {
        UserDaoJPA jpa = new UserDaoJPA();
        jpa.setEntityManager(em);
        jpa.setTweetService(tService);
        userDao = jpa;
    }

    @Test
    public void addUser() {
        User user = new User("testUsername", "testName", "testweb", "testbio");
        userDao.addUser(user);

        ArgumentCaptor<User> parameterCaptor = ArgumentCaptor.forClass(User.class);
        verify(em, times(1)).persist(parameterCaptor.capture());

        User instance = parameterCaptor.getValue();
        Assert.assertEquals("testUsername", instance.getUsername());
    }

    @Test
    public void removeUser() throws UserNotFoundException {
        User user = new User("testUsername", "testName", "testweb", "testbio");

        when(tService.getTweetsByUsername(user.getUsername())).thenThrow(UserNotFoundException.class);

        userDao.removeUser(user);

        ArgumentCaptor<User> parameterCaptor = ArgumentCaptor.forClass(User.class);
        verify(em, times(1)).remove(parameterCaptor.capture());

        User instance = parameterCaptor.getValue();
        Assert.assertEquals("testUsername", instance.getUsername());
    }

    @Test
    public void getUsers() {
        User willem = new User("Willem", "Willem Toemen", "test", "test");
        User ken = new User("Ken", "Ken van Grinsven", "test", "test");

        ArrayList<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(willem);
        expectedUsers.add(ken);

        TypedQuery<User> mockedQuery = mock(TypedQuery.class);
        when(em.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(expectedUsers);

        Assert.assertEquals(expectedUsers, userDao.getUsers());
    }

    @Test
    public void findByUsername() throws UserNotFoundException {
        User willem = new User("Willem", "Willem Toemen", "test", "test");
        User ken = new User("Ken", "Ken van Grinsven", "test", "test");

        ArrayList<User> usersToFind = new ArrayList<>();
        usersToFind.add(willem);
        usersToFind.add(ken);

        TypedQuery<User> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("user.findByName", User.class)).thenReturn(mockedQuery);

        when(mockedQuery.getResultList()).thenReturn(new ArrayList() {{
            add(willem);
        }});
        User foundUser = userDao.findByUsername(willem.getUsername());
        Assert.assertEquals(willem.getName(), foundUser.getName());
        verify(mockedQuery, times(1)).setParameter("username", willem.getUsername());


        when(mockedQuery.getResultList()).thenReturn(new ArrayList() {{
            add(ken);
        }});
        foundUser = userDao.findByUsername(ken.getUsername());
        Assert.assertEquals(ken.getName(), foundUser.getName());
        verify(mockedQuery, times(1)).setParameter("username", ken.getUsername());

    }
    @Test (expected = UserNotFoundException.class)
    public void findByUsernameUserNotFoundException() throws UserNotFoundException {
        TypedQuery<User> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("user.findByName", User.class)).thenReturn(mockedQuery);

        when(mockedQuery.getResultList()).thenReturn(new ArrayList<>());
        User foundUser = userDao.findByUsername("fakeUsername");
        Assert.assertEquals(null, foundUser.getName());
        verify(mockedQuery, times(1)).setParameter("username", "fakeUsername");
    }
}
