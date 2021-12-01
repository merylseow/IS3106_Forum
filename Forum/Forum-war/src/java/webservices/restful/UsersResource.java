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
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.stateless.ForumUserSessionLocal;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;

/**
 * REST Web Service
 *
 * @author merylseow
 */
@Path("users")
public class UsersResource {

    @EJB
    ForumUserSessionLocal forumUserSessionLocal;

    public UsersResource() {
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") Long userId) {
        try {
            ForumUser u = forumUserSessionLocal.retrieveUserByUserId(userId);
            return Response.ok().entity(u).type(MediaType.APPLICATION_JSON).build();
        } catch (UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found").build();
            return Response.status(404).entity(exception).type(MediaType.APPLICATION_JSON).build();
        }
    }
    
    @GET
    @Path("/allusers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return Response.ok().entity(forumUserSessionLocal.retrieveAllUsers()).build();
    }

    @PUT
    @Path("/edit/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(@PathParam("userId") Long userId, ForumUser u) {
        u.setId(userId);
        try {
            forumUserSessionLocal.updateUser(u);
            return Response.ok().build();
        } catch (UpdateUserException | UserNotFoundException ex) {
            JsonObject exception = Json.createObjectBuilder().add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @DELETE
    @Path("/delete/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") Long userId) {
        try {
            forumUserSessionLocal.deleteUser(userId);
            return Response.ok().build();
        } catch (UserNotFoundException ex) {
            JsonObject exception = Json.createObjectBuilder().add("error", "Post not found").build();
            return Response.status(404).entity(exception).build();
        }
    }

    @PUT
    @Path("/block/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response blockUser(@PathParam("userId") Long userId) {
        try {
            forumUserSessionLocal.blockUser(userId);
            return Response.ok().build();
        } catch (UserNotFoundException | UpdateUserException ex) {
            JsonObject exception = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return Response.status(400).entity(exception).build();
        }
    }

    @PUT
    @Path("/unblock/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unblockUser(@PathParam("userId") Long userId) {
        try {
            forumUserSessionLocal.unblockUser(userId);
            return Response.ok().build();
        } catch (UserNotFoundException | UpdateUserException ex) {
            JsonObject exception = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return Response.status(400).entity(exception).build();
        }
    }
}