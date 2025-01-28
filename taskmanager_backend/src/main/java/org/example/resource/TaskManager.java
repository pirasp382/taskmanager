package org.example.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.example.dto.*;

import java.util.List;

@Path("")
@ApplicationScoped
public interface TaskManager {

  @POST
  @Path("/registerUser")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "register user",
      description =
          "Registers a new user in the application. The user is checked and, if validation is successful,"
              + " the user is saved in the database. A token and user information are returned in response.")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = LoginOutput.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid or missing input data",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = List.class)))
  @Transactional
  Response registerUser(final User user);

  @Path("/login")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "login user",
      description = " Logs in a user and returns the login data and the access token")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = LoginOutput.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid or missing input data",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = List.class)))
  @Transactional
  Response login(final LoginInput input);

  @Path("/getUserData")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "get user data",
      description = "get all the user data from the database to the frontend")
  Response getUserData(@HeaderParam("Authorization") final String authHeader);

  @Path("/editProfile")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "edit user profile",
      description = "Updates a user's information based on the provided token and profile changes.")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = UserOutput.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid or missing input data",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = List.class)))
  @Transactional
  Response editUser(
      @HeaderParam("Authorization") final String authHeader, final UpdateProfile profile);

  @Path("/task")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "gett all user tasks", description = "get all the tasks by a user id")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = UserOutput.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid or missing input data",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = List.class)))
  @Transactional
  Response getAllTasks(@HeaderParam("Authorization") final String authHeader);

  @Path("/task/tasks/{id}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "gett single Task by TaskId",
      description = "get the single task by taskid and userId from accesstoken")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Task.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid or missing input data",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = List.class)))
  @Transactional
  Response getTaskById(
      @HeaderParam("Authorization") final String authHeader, @PathParam("id") final long id);

  @Path("/task")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "add Task", description = "add a task from a user")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Task.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid or missing input data",
      content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = List.class)))
  @Transactional
  Response addTask(@HeaderParam("Authorization") final String authHeader, final Task task);

  @Path("/task/sort")
  @GET
  Response getTasksSorted(
      @HeaderParam("Authorization") final String authHeader,
      @QueryParam("sorted") final String sortValue,
      @QueryParam("direction") final boolean direction);

  @Path("/dashboard/pie")
  @GET
  Response hello(
      @HeaderParam("Authorization") final String authHeader,
      @QueryParam("countByValue") final String countByValue);
}
