package dao.JPA;

import dao.HashtagDao;
import domain.Hashtag;
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
public class HashtagDaoJPATest {

    private HashtagDao tagDao;

    @Mock
    private EntityManager em;

    @Before
    public void setUp() {
        HashtagDaoJPA jpa = new HashtagDaoJPA();
        jpa.setEntityManager(em);
        tagDao = jpa;
    }

    @Test
    public void findByName() {
        Hashtag expected = new Hashtag("testtag");

        ArrayList<Hashtag> returnList = new ArrayList<>();
        returnList.add(expected);

        TypedQuery<Hashtag> mockedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("hashtag.findByName", Hashtag.class)).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(returnList);

        Hashtag actual = tagDao.findByName(expected.getName());

        verify(mockedQuery, times(1)).setParameter("name", expected.getName());
        Assert.assertEquals(expected, actual);

        // Test if an unknown tag is requested. Should return a new tag
        when(mockedQuery.getResultList()).thenReturn(new ArrayList<>());

        Hashtag newActual = tagDao.findByName("newTag");

        verify(mockedQuery, times(1)).setParameter("name", "newTag");
        Assert.assertEquals("newTag", newActual.getName());
    }
}