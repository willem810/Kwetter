package rest;

import domain.Hashtag;
import domain.Permissions;
import interceptor.Authentication;
import service.HashtagService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("hashtags")
@Stateless
@Authentication({Permissions.hashtag_guest})
public class HashtagResource {
    @Inject
    HashtagService hService;

    public void setHashtagService(HashtagService hService) {
        this.hService = hService;
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addHashtag(Hashtag hashtag){
        hService.addHashtag(hashtag);
        return Response.ok().build();
    }
    @POST
    @Path("/remove/{name}")
    @Authentication({Permissions.hashtag_moderator})
    public Response removeHashtag(@PathParam("name") String name){
        Hashtag tag = hService.findByName(name);
        hService.removeHashtag(tag);

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHashtags(){
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Hashtag> all = this.hService.getHashtags();
        all.stream().map(Hashtag::toJson).forEach(list::add);

        return Response.ok(list.build()).build();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByName(@PathParam("name") String name) {
        Hashtag tag = hService.findByName(name);
        return Response.ok(tag.toJson()).build();
    }
}
