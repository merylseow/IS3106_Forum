package webservices.restful;

import entity.Forum;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.stateless.ForumSessionLocal;
import util.exception.ForumExistsException;
import util.exception.NoResultException;
import util.exception.ThreadNotFoundException;

/**
 * REST Web Service
 *
 * @author merylseow
 */
@Path("forums")
public class ForumsResource {

    @EJB
    ForumSessionLocal forumSessionLocal;

    public ForumsResource() {
    }

    @GET
    @Path("/{forumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForum(@PathParam("forumId") Long forumId) {
        try {
            Forum f = forumSessionLocal.retrieveForumById(forumId);
            System.out.println(f);
            return Response.status(200).entity(f).type(MediaType.APPLICATION_JSON).build();
        } catch (NoResultException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found").build();
            return Response.status(404).entity(exception).type(MediaType.APPLICATION_JSON).build();
        }
    }
    
    @GET
    @Path("/allforums")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllForums() {
        return Response.ok().entity(forumSessionLocal.retrieveAllForums()).build();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createForum(Forum f) {
        try {
            forumSessionLocal.createForum(f);
            return Response.ok().entity(f).build();
        } catch (ForumExistsException e) {
            return Response.status(400).entity("{\"errorMessage\":\"Forum already exists!\"}").build();
        }
    }
    
    @GET
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchForums(@QueryParam("name") String name,
            @QueryParam("description") String description) {

        if (name != null) {
            List<Forum> results = forumSessionLocal.searchForums(name);
            GenericEntity<List<Forum>> entity = new GenericEntity<List<Forum>>(results) {
            };

            return Response.status(200).entity(entity).build();
        
        } else if (description != null) {
            List<Forum> results = forumSessionLocal.searchForumsByDescription(description);
            GenericEntity<List<Forum>> entity = new GenericEntity<List<Forum>>(results) {
            };

            return Response.status(200).entity(entity).build();
        } else {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "No query conditions")
                    .build();

            return Response.status(400).entity(exception).build();
        }
    }
    

    @PUT
    @Path("/edit/{forumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editForum(@PathParam("forumId") Long forumId, Forum f) {
        f.setId(forumId);
        try {
            forumSessionLocal.updateForum(f);
            return Response.status(204).build();
        } catch (NoResultException e) {
            JsonObject exception = Json.createObjectBuilder().add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception).type(MediaType.APPLICATION_JSON).build();
        } catch (ForumExistsException ex) {
            return Response.status(400).entity("{\"errorMessage\":\"Forum already exists!\"}").build();
        }
    }

    @DELETE
    @Path("/delete/{forumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteForum(@PathParam("forumId") Long forumId) {
        try {
            forumSessionLocal.deleteForum(forumId);
            return Response.status(204).build();
        } catch (NoResultException | ThreadNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder().add("error", "Forum not found").build();
            return Response.status(404).entity(exception).build();
        }
    }
}
