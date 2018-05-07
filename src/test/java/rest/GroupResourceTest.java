package rest;

import domain.Group;
import domain.Permissions;
import exception.GroupNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import service.GroupService;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupResourceTest {
    GroupResource rResource = new GroupResource();

    @Mock
    GroupService rService;

    @Before
    public void setUp(){ rResource.setGroupService(rService);}

    @Test
    public void addGroup() {
        ArrayList<Permissions> perms = new ArrayList<>();
        perms.add(Permissions.user_basic);
        perms.add(Permissions.tweet_basic);
        Group g = new Group("testGroup", perms);

        Response resp = rResource.addGroup(g);

        Assert.assertEquals(200, resp.getStatus());
        verify(rService, times(1)).addGroup(g);
    }

    @Test
    public void removeGroup() throws GroupNotFoundException {
        ArrayList<Permissions> perms = new ArrayList<>();
        perms.add(Permissions.user_basic);
        perms.add(Permissions.tweet_basic);
        Group g = new Group("testGroup", perms);

        when(rService.findByName("testGroup")).thenReturn(g);

        Response resp = rResource.removeGroup(g.getName());

        Assert.assertEquals(200, resp.getStatus());
        verify(rService, times(1)).removeGroup(g);
    }

    @Test
    public void removeGroupGroupNotFoundException() throws GroupNotFoundException {
        when(rService.findByName("fakeGroup")).thenThrow(GroupNotFoundException.class);

        Response resp = rResource.removeGroup("fakeGroup");

        Assert.assertEquals(404, resp.getStatus());
    }

    @Test
    public void getGroup() {
        ArrayList<Permissions> perms = new ArrayList<>();
        perms.add(Permissions.user_basic);
        perms.add(Permissions.tweet_basic);

        ArrayList<Group> expectedList = new ArrayList();
        for (int i = 0; i< 3; i++) {
            Group g = new Group("testGroup" + i, perms);
            g.setId(new Long(i));
            expectedList.add(g);
        }

        when(rService.getGroups()).thenReturn(expectedList);

        Response resp = rResource.getGroups();

        Assert.assertEquals(200, resp.getStatus());
        JsonArray list = (JsonArray) resp.getEntity();

        for(int i = 0; i<expectedList.size(); i++) {
            Assert.assertEquals(expectedList.get(i).toJson(), list.getJsonObject(i));
        }
    }

    @Test
    public void findByName() throws GroupNotFoundException {
        ArrayList<Permissions> perms = new ArrayList<>();
        perms.add(Permissions.user_basic);
        perms.add(Permissions.tweet_basic);

        Group g = new Group("testGroup", perms);
        g.setId(1L);

        when(rService.findByName(g.getName())).thenReturn(g);

        Response resp = rResource.findByName(g.getName());
        JsonObject actualObj = (JsonObject) resp.getEntity();

        Assert.assertEquals(200, resp.getStatus());
        Assert.assertEquals(g.getName(), actualObj.getString("name"));
    }

    @Test
    public void findByNameGroupNotFoundException() throws GroupNotFoundException {


        when(rService.findByName("fakeGroup")).thenThrow(GroupNotFoundException.class);

        Response resp = rResource.findByName("fakeGroup");

        Assert.assertEquals(404, resp.getStatus());
    }
}