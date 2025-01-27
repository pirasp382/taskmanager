package org.example.resource;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.example.dto.*;
import org.example.dto.database.TaskEntity;
import org.example.mapper.TaskEntityMapper;
import org.example.mapper.UserEntiyMapper;
import org.example.repository.TaskRepository;
import org.example.repository.UserRepository;
import org.example.services.validation.TokenValidation;

@Path("")
@ApplicationScoped
public class AuthBackendResource {

  @Inject TokenValidation tokenValidation;
  @Inject JWTParser parser;

  final UserRepository userRepository = new UserRepository();
  final TaskRepository taskRepository = new TaskRepository();

  @Path("/task/{id}")
  @PUT
  @Transactional
  public Response updateTask(
      @HeaderParam("Authorization") final String authHeader,
      @PathParam("id") final long id,
      final UpdateTask task)
      throws ParseException {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    final JsonWebToken token = parser.parseOnly(authHeader.substring("Bearer:".length()).trim());
    final var user = userRepository.findByName(token.getClaim("username"));
    final List<TaskEntity> taskEntityList = taskRepository.getTaskByIdAndUserId(id, user);
    if (taskEntityList.isEmpty()) {
      errorList.add(Message.builder().title("Task does not exists").build());
      return Response.ok(errorList).build();
    }
    final TaskEntity taskEntity = taskEntityList.getFirst();
    if (task.getTitle() != null) {
      taskEntity.setTitle(task.getTitle());
    }
    if (task.getDescription() != null) {
      taskEntity.setDescription(task.getDescription());
    }
    if (task.getStatus().getTitle() != null) {
      taskEntity.setStatus(TaskEntityMapper.getStatusEntity(task.getStatus()));
    }
    if (task.getPriority().getTitle() != null) {
      taskEntity.setPriority(TaskEntityMapper.getPriorityEntity(task.getPriority()));
    }
    taskEntity.setLastUpdate(LocalDateTime.now());
    taskEntity.persist();
    final Task resultTask = TaskEntityMapper.mapToTask(taskEntity);
    return Response.ok(resultTask).build();
  }

  @Path("/task/{id}")
  @DELETE
  @Transactional
  public Response deleteTask(
      @HeaderParam("Authorization") final String authHeader, @PathParam("id") final long id)
      throws ParseException {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    final JsonWebToken token = parser.parseOnly(authHeader.substring("Bearer:".length()).trim());
    final var user = userRepository.findByName(token.getClaim("username"));
    final List<TaskEntity> taskEntityList = taskRepository.getTaskByIdAndUserId(id, user);
    if (taskEntityList.isEmpty()) {
      errorList.add(Message.builder().title("Task does not exists").build());
      return Response.ok(errorList).build();
    }
    final String result =
        (taskRepository.deleteById(id, user) == 1L
            ? "task sucessfully deleted"
            : "task could not get deleted");
    return Response.ok(Map.of("message", result)).build();
  }

  @Path("/hello")
  @POST
  public Response helloWorld(final User user) {
    return Response.accepted(user).build();
  }
}
