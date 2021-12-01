/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.ForumThread;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.stateless.ThreadSessionLocal;
import util.exception.NoResultException;
import util.exception.ThreadExistsException;
import util.exception.ThreadNotFoundException;
import util.exception.UserNotFoundException;

/**
 * REST Web Service
 *
 * @author merylseow
 */
@Path("threads")
public class ThreadsResource {

    @EJB
    ThreadSessionLocal threadSessionLocal;

    public ThreadsResource() {
    }

    @GET
    @Path("/{threadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getThread(@PathParam("threadId") Long threadId) {
        try {
            ForumThread t = threadSessionLocal.retrieveThreadById(threadId);
            return Response.ok().entity(t).type(MediaType.APPLICATION_JSON).build();
        } catch (ThreadNotFoundException e) {
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/threadsInForum/{forumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getThreadsInForum(@PathParam("forumId") Long forumId) {
        System.out.println(forumId);
        System.out.println(threadSessionLocal.retrieveThreadsByForumId(forumId).size());
        return Response
                .ok()
                .entity(threadSessionLocal.retrieveThreadsByForumId(forumId))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("/create/{userId}/{forumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createThread(ForumThread t,
            @PathParam("userId") Long userId,
            @PathParam("forumId") Long forumId) {
        try {
            threadSessionLocal.createThread(t, forumId, userId);
        } catch (NoResultException | UserNotFoundException | ThreadNotFoundException ex) {
            return Response.status(400).build();
        } catch (ThreadExistsException e) {
            return Response.status(400).entity("{\"errorMessage\":\"Thread already exists!\"}").build();
        } 
        return Response.ok().build();
    }

    @PUT
    @Path("/edit/{threadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editThread(@PathParam("threadId") Long threadId, ForumThread t) {
        t.setId(threadId);
        try {
            threadSessionLocal.updateThread(t);
            return Response.status(204).build();
        } catch (ThreadNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder().add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception).type(MediaType.APPLICATION_JSON).build();
        } catch (ThreadExistsException ex) {
            return Response.status(400).entity("{\"errorMessage\":\"Thread already exists!\"}").build();
        }
    }

    @DELETE
    @Path("/delete/{threadId}/{forumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteThread(@PathParam("threadId") Long threadId, @PathParam("forumId") Long forumId) {
        try {
            threadSessionLocal.deleteThread(threadId, forumId);
            return Response.ok().build();
        } catch (ThreadNotFoundException | NoResultException e) {
            JsonObject exception = Json.createObjectBuilder().add("error", "Thread not found").build();
            return Response.status(404).entity(exception).build();
        }
    }

    @PUT
    @Path("/close/{threadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response closeThread(@PathParam("threadId") Long threadId) {
        try {
            threadSessionLocal.closeThread(threadId);
            return Response.ok().build();
        } catch (ThreadNotFoundException ex) {
            JsonObject exception = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return Response.status(404).entity(exception).build();
        } catch (ThreadExistsException ex) {
            return Response.status(400).entity("{\"errorMessage\":\"Thread already exists!\"}").build();
        }
    }

    @PUT
    @Path("/open/{threadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response openThread(@PathParam("threadId") Long threadId) {
        try {
            threadSessionLocal.openThread(threadId);
            return Response.ok().build();
        } catch (ThreadNotFoundException ex) {
            JsonObject exception = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return Response.status(404).entity(exception).build();
        } catch (ThreadExistsException ex) {
            return Response.status(400).entity("{\"errorMessage\":\"Thread already exists!\"}").build();
        }
    }
}
