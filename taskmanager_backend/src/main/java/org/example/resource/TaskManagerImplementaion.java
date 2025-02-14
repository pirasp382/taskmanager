package org.example.resource;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.example.dto.*;
import org.example.dto.database.TaskEntity;
import org.example.mapper.TaskEntityMapper;
import org.example.mapper.UserEntiyMapper;
import org.example.repository.TaskRepository;
import org.example.repository.UserRepository;
import org.example.services.validation.*;
import org.example.util.Encryption;
import org.example.util.Util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
            .fullname(Encryption.decrypt(user.getFullname(), user.getPassword()))
            .email(Encryption.decrypt(user.getEmail(), user.getPassword()))
            .bio(user.getBio())
            .token(Util.createToken(user))
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
      if (EditUserValidation.usernameIsUnique(profile.getUsername())) {
        user.setUsername(profile.getUsername());
      } else {
        errorList.add(Message.builder().title("Username already exists").build());
      }
    }
    if (profile.getPassword() != null) {
      user.setPassword(Util.hashpassword(profile.getPassword()));
    }
    if (profile.getBio() != null) {
      user.setBio(profile.getBio());
    }
    if (profile.getFullname() != null) {
      user.setFullname(Encryption.encrypt(profile.getFullname(), user.getPassword()));
    }
    if (profile.getAvatarUrl() != null) {
      user.setAvatarUrl(profile.getAvatarUrl());
    }
    if (profile.getEmail() != null) {
      if (EditUserValidation.emailIsUnique(profile.getEmail())) {
        user.setEmail(Encryption.encrypt(profile.getEmail(), user.getPassword()));
      } else {
        errorList.add(Message.builder().title("Email is not unique").build());
      }
    }
    if (errorList.isEmpty()) {
      user.persist();
    }
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
    final var userOutput =
        UserOutput.builder().taskList(taskList).token(Util.createToken(user)).build();
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
    final TaskList taskList =
        TaskList.builder().taskList(task).token(Util.createToken(user)).build();
    return Response.ok(taskList).build();
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
    final TaskList taskList =
        TaskList.builder().taskList(List.of(resultTask)).token(Util.createToken(user)).build();
    return Response.ok(taskList).build();
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
    final TaskList taskList =
        TaskList.builder().taskList(result).token(Util.createToken(user)).build();
    return Response.ok(taskList).build();
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
    final TaskDetailOutput taskDetailOutput =
        TaskDetailOutput.builder().tasksDetails(result).token(Util.createToken(user)).build();
    return Response.ok(taskDetailOutput).build();
  }

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
    if (task.getTitle() != null && !task.getTitle().isEmpty()) {
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
    final TaskList taskList =
        TaskList.builder().taskList(List.of(resultTask)).token(Util.createToken(user)).build();
    return Response.ok(taskList).build();
  }
}
