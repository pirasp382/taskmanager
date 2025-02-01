package org.example.resource;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.example.dto.*;
import org.example.dto.database.TaskEntity;
import org.example.mapper.TaskEntityMapper;
import org.example.mapper.UserEntiyMapper;
import org.example.repository.TaskRepository;
import org.example.repository.UserRepository;
import org.example.services.validation.LoginValidation;
import org.example.services.validation.RegistrationValidation;
import org.example.services.validation.TaskValidation;
import org.example.services.validation.TokenValidation;
import org.example.util.Util;

import java.time.LocalDateTime;
import java.util.List;

public class TaskManagerImplementaion implements TaskManager {

  @Inject TokenValidation tokenValidation;
  @Inject JWTParser parser;

  final UserRepository userRepository = new UserRepository();
  final UserEntiyMapper mapper = new UserEntiyMapper();
  final TaskRepository taskRepository = new TaskRepository();

  private Long getUserIdFromToken(final String authHeader) {
    try {
      final JsonWebToken token = parser.parseOnly(authHeader.substring("Bearer:".length()).trim());
      return Long.parseLong(token.getSubject());
    } catch (final ParseException exception) {
      return -1L;
    }
  }

  @Transactional
  public Response registerUser(final User user) {
    final List<Message> errorList = RegistrationValidation.validate(user);
    if (!errorList.isEmpty()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(errorList).build();
    }
    final var userEntity = mapper.mapToUserEntityRegistration(user);
    new UserRepository().persist(userEntity);
    final RegistrationOutput registrationOutput =
        RegistrationOutput.builder()
            .username(userEntity.getUsername())
            .token(Util.createToken(userEntity))
            .build();
    return Response.ok(registrationOutput).build();
  }

  public Response login(final LoginInput input) {
    final List<Message> errorList = LoginValidation.validate(input);
    if (!errorList.isEmpty()) {
      final LoginOutput loginOutput = LoginOutput.builder().errorlist(errorList).build();
      return Response.ok(loginOutput).build();
    }
    final var user = userRepository.findByName(input.getUsername());
    user.setLastLogin(LocalDateTime.now());
    PanacheEntityBase.persist(user);
    final LoginOutput loginOutput = mapper.mapToLoginOutput(user);
    return Response.accepted(loginOutput).build();
  }

  public Response getUserData(@HeaderParam("Authorization") final String authHeader) {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    final var user = userRepository.findByUserID(getUserIdFromToken(authHeader));
    final var userData =
        UserData.builder()
            .username(user.getUsername())
            .fullname(user.getFullname())
            .email(user.getEmail())
            .bio(user.getBio())
            .build();
    return Response.ok(userData).build();
  }

  @Transactional
  public Response editUser(
      @HeaderParam("Authorization") final String authHeader, final UpdateProfile profile) {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    final var user = userRepository.findByUserID(getUserIdFromToken(authHeader));
    if (profile.getUsername() != null) {
      user.setUsername(profile.getUsername());
    }
    if (profile.getPassword() != null) {
      user.setPassword(Util.hashpassword(profile.getPassword()));
    }
    if (profile.getBio() != null) {
      user.setBio(profile.getBio());
    }
    if (profile.getFullname() != null) {
      user.setFullname(profile.getFullname());
    }
    if (profile.getAvatarUrl() != null) {
      user.setAvatarUrl(profile.getAvatarUrl());
    }
    if (profile.getEmail() != null) {
      user.setEmail(profile.getEmail());
    }
    user.persist();
    final var userOutput = mapper.mapToLoginOutput(user);
    return Response.ok(userOutput).build();
  }

  public Response getAllTasks(@HeaderParam("Authorization") final String authHeader) {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    final var user = userRepository.findByUserID(getUserIdFromToken(authHeader));
    final var taskList =
        taskRepository.getAllTasksByUserId(user).stream().map(TaskEntityMapper::mapToTask).toList();
    final UserOutput userOutput = UserOutput.builder().taskList(taskList).build();
    return Response.ok(userOutput).build();
  }

  public Response getTaskById(
      @HeaderParam("Authorization") final String authHeader, @PathParam("id") final long id) {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    final var user = userRepository.findByUserID(getUserIdFromToken(authHeader));
    final var task =
        taskRepository.getTaskByIdAndUserId(id, user).stream()
            .map(TaskEntityMapper::mapToTask)
            .toList();
    return Response.ok(task).build();
  }

  @Transactional
  public Response addTask(@HeaderParam("Authorization") final String authHeader, final Task task) {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    final var user = userRepository.findByUserID(getUserIdFromToken(authHeader));
    final TaskEntity taskEntity = TaskEntityMapper.mapToTaskEntity(task, user);
    taskEntity.persist();
    final Task resultTask = TaskEntityMapper.mapToTask(taskEntity);
    return Response.ok(resultTask).build();
  }

  public Response getTasksSorted(
      @HeaderParam("Authorization") final String authHeader,
      @QueryParam("sorted") final String sortValue,
      @QueryParam("ascending") final boolean direction) {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    if (!TaskValidation.tableContainsColumn(sortValue)) {
      errorList.add(Message.builder().title("Sortparamter invalid").build());
      return Response.ok(errorList).build();
    }
    final var user = userRepository.findByUserID(getUserIdFromToken(authHeader));
    final var result =
        taskRepository.getSortedTaskList(user, sortValue, direction).stream()
            .map(TaskEntityMapper::mapToTask)
            .toList();
    return Response.ok(result).build();
  }

  public Response hello(
      @HeaderParam("Authorization") final String authHeader,
      @QueryParam("countByValue") final String countByValue) {
    final List<Message> errorList = tokenValidation.validateToken(authHeader);
    if (!errorList.isEmpty()) {
      return Response.ok(errorList).build();
    }
    if (!TaskValidation.tableContainsColumn(countByValue)) {
      errorList.add(Message.builder().title("Sortparamter invalid").build());
      return Response.ok(errorList).build();
    }
    final var user = userRepository.findByUserID(getUserIdFromToken(authHeader));
    final List<TasksDetail> result =
        (countByValue.equals("status"))
            ? taskRepository.getTasksCountByStatus(user)
            : taskRepository.getTasksCountByPriority(user);
    return Response.ok(result).build();
  }
}
