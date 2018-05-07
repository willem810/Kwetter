package service;

import dao.GroupDao;
import domain.Group;
import exception.GroupNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {
    GroupService rService;

    @Mock
    GroupDao groupDao;

    @Before
    public void setUp(){
        rService = new GroupService();
        rService.setGroupDao(groupDao);
    }

    @Test
    public void addGroup() {
        Group r1 = new Group();
        Group r2 = new Group();

        rService.addGroup(r1);
        rService.addGroup(r2);

        verify(groupDao, times(1)).addGroup(r1);
        verify(groupDao, times(1)).addGroup(r2);
    }

    @Test
    public void removeGroup() {
        Group r1 = new Group();
        Group r2 = new Group();

        rService.removeGroup(r1);
        rService.removeGroup(r2);

        verify(groupDao, times(1)).removeGroup(r1);
        verify(groupDao, times(1)).removeGroup(r2);
    }

    @Test
    public void getGroups() {
        Group r1 = new Group();
        Group r2 = new Group();

        ArrayList<Group> expected = new ArrayList<>();
        expected.add(r1);
        expected.add(r2);

        when(groupDao.getGroups()).thenReturn(expected);

        Assert.assertEquals(expected, rService.getGroups());
    }

    @Test
    public void findByName() throws GroupNotFoundException {
        Group r1 = new Group();
        Group r2 = new Group();

        when(groupDao.findByName("g1")).thenReturn(r1);
        when(groupDao.findByName("g2")).thenReturn(r2);

        Group r1Found = rService.findByName("g1");
        Group r2Found = rService.findByName("g2");

        Assert.assertEquals(r1, r1Found);
        Assert.assertEquals(r2, r2Found);

    }

    @Test (expected = GroupNotFoundException.class)
    public void findByNameGroupNotFoundException() throws GroupNotFoundException {
        when(groupDao.findByName("fakeGroup")).thenThrow(GroupNotFoundException.class);
        rService.findByName("fakeGroup");
    }
}