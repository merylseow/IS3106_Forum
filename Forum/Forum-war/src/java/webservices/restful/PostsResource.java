/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.Post;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.stateless.PostSessionLocal;
import util.exception.NoResultException;
import util.exception.PostExistsException;
import util.exception.PostNotFoundException;
import util.exception.ThreadNotFoundException;
import util.exception.UserNotFoundException;

/**
 * REST Web Service
 *
 * @author merylseow
 */
@Path("posts")
public class PostsResource {

    @EJB
    PostSessionLocal postSessionLocal;

    public PostsResource() {
    }

    @GET
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postId") Long postId) {
        try {
            Post p = postSessionLocal.retrievePostById(postId);
            return Response.ok().entity(p).type(MediaType.APPLICATION_JSON).build();
        } catch (PostNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found").build();
            return Response.status(404).entity(exception).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/postsInThread/{threadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsInThread(@PathParam("threadId") Long threadId) {
        return Response.ok().entity(postSessionLocal.retrievePostsByThreadId(threadId)).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/create/{userId}/{threadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(Post p,
            @PathParam("userId") Long userId,
            @PathParam("threadId") Long threadId) {
        try {
            postSessionLocal.createPost(p, threadId, userId);
        } catch (PostNotFoundException ex) {
            return Response.status(404).build();
        } catch (ThreadNotFoundException ex) {
            return Response.status(403).build();         
        } catch (UserNotFoundException ex) {
            return Response.status(400).build();
        } catch (PostExistsException ex) {
            return Response.status(400).entity("{\"errorMessage\":\"Post already exists!\"}").build();
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/edit/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPost(@PathParam("postId") Long postId, Post p) {
        p.setId(postId);
        try {
            postSessionLocal.updatePost(p);
            return Response.ok().build();
        } catch (PostNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder().add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception).type(MediaType.APPLICATION_JSON).build();
        } catch (PostExistsException ex) {
            return Response.status(400).entity("{\"errorMessage\":\"Post already exists!\"}").build();
        }
    }

    @DELETE
    @Path("/delete/{postId}/{threadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePost(@PathParam("postId") Long postId, @PathParam("threadId") Long threadId) {
        try {
            postSessionLocal.deletePost(postId, threadId);
            return Response.ok().build();
        } catch (NoResultException | ThreadNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder().add("error", "Post not found").build();
            return Response.status(404).entity(exception).build();
        }
    }
}
