/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.ForumUser;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.stateless.ForumUserSessionLocal;
import util.exception.UserExistsException;
import util.exception.UserNotFoundException;

/**
 * REST Web Service
 *
 * @author merylseow
 */
@Path("user")
public class UserResource {

    @EJB
    private ForumUserSessionLocal forumUserSessionLocal;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(ForumUser u) throws UserNotFoundException {
        if (forumUserSessionLocal.userLogin(u.getUsername(), u.getPassword())) {
            ForumUser user = forumUserSessionLocal.retrieveUserByUsername(u.getUsername());
            return Response.ok().entity(user).build();
        } else {
            JsonObject exception = Json.createObjectBuilder().add("error", "Not found").build();
            return Response.status(404).entity(exception).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(ForumUser u) {
        try {
            forumUserSessionLocal.createUser(u);
            return Response.ok().entity(u).build();
        } catch (UserExistsException ex) {
            return Response.status(400).entity("{\"errorMessage\":\"Username is not valid. Pick another username.\"}").build();
        }
    }
}
