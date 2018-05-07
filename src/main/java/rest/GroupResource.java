package rest;

import domain.Group;
import domain.Permissions;
import exception.GroupNotFoundException;
import interceptor.Authentication;
import service.GroupService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("groups")
@Stateless
@Authentication({Permissions.group_basic})
public class GroupResource {
    @Inject
    GroupService rService;

    public void setGroupService(GroupService rService) {
        this.rService = rService;
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Authentication(Permissions.group_admin)
    public Response addGroup(Group group) {
        rService.addGroup(group);
        return Response.ok().build();
    }

    @POST
    @Path("/remove/{name}")
    @Authentication(Permissions.group_admin)
    public Response removeGroup(@PathParam("name") String name) {
        Group g = null;
        try {
            g = rService.findByName(name);
        } catch(GroupNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        rService.removeGroup(g);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authentication(Permissions.group_moderator)
    public Response getGroups() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Group> all = this.rService.getGroups();
        all.stream().map(Group::toJson).forEach(list::add);

        return Response.ok(list.build()).build();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByName(@PathParam("name") String name) {
        Group g = null;

        try {
            g = rService.findByName(name);
        }catch(GroupNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

        return Response.ok(g.toJson()).build();
    }
}
