package dao.JPA;

import dao.GroupDao;
import domain.Group;
import exception.GroupNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupDaoJPATest {

    private GroupDao groupDao;

    @Mock
    private EntityManager em;

    @Before
    public void setUp() {
        GroupDaoJPA jpa = new GroupDaoJPA();
        jpa.setEntityManager(em);
        groupDao = jpa;
    }


    @Test
    public void findByName() throws GroupNotFoundException {
        Group g = new Group("newGroup", new ArrayList<>());

        ArrayList<Group> returnList = new ArrayList<>();
        returnList.add(g);

        TypedQuery<Group> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("group.findByName", Group.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(returnList);

        Group actual = groupDao.findByName(g.getName());

        verify(mockedQuery, times(1)).setParameter("name", g.getName());
        Assert.assertEquals(g, actual);
    }

    @Test (expected = GroupNotFoundException.class)
    public void findByNameGroupNotFound() throws GroupNotFoundException {
        TypedQuery<Group> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("group.findByName", Group.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(new ArrayList<>());

        groupDao.findByName("fakeGroup");

    }
}