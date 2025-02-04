package org.example.mapper;

import java.time.LocalDateTime;

import org.example.dto.LoginOutput;
import org.example.dto.Priority;
import org.example.dto.Status;
import org.example.dto.User;
import org.example.dto.database.PriorityEntity;
import org.example.dto.database.StatusEntity;
import org.example.dto.database.UserEntity;
import org.example.repository.PriorityRepository;
import org.example.repository.StatusRepository;
import org.example.repository.TaskRepository;
import org.example.util.Encryption;
import org.example.util.Util;

public class UserEntiyMapper {

  final StatusRepository statusRepository = new StatusRepository();
  final PriorityRepository priorityRepository = new PriorityRepository();
  final TaskRepository taskRepository = new TaskRepository();

  public UserEntity mapToUserEntityRegistration(final User user) {
    final var hashedPassword = Util.hashpassword(user.getPassword());
    return UserEntity.builder()
        .username(user.getUsername())
        .password(hashedPassword)
        .fullname(Encryption.encrypt(user.getFullname(), hashedPassword))
        .bio(user.getBio())
        .avatarUrl(user.getAvatarUrl())
        .lastLogin(user.getLastLogin())
        .createdAt(getUserCreatedAt(user))
        .email(Encryption.encrypt(user.getEmail(), hashedPassword))
        .statuses(statusRepository.getInitalStatusItems())
        .priorities(priorityRepository.getInitalPriorityItems())
        .build();
  }

  public LoginOutput mapToLoginOutput(final UserEntity userEntity) {
    return LoginOutput.builder()
        .username(userEntity.getUsername())
        .fullname(userEntity.getFullname())
        .bio(userEntity.getBio())
        .avatarUrl(userEntity.getAvatarUrl())
        .statusEntities(
            statusRepository.getAll(userEntity).stream().map(UserEntiyMapper::mapToStatus).toList())
        .priorityEntities(
            priorityRepository.getAll(userEntity).stream()
                .map(UserEntiyMapper::mapToPriority)
                .toList())
        .token(Util.createToken(userEntity))
        .taskList(
            taskRepository.getAllTasksByUserId(userEntity).stream()
                .map(TaskEntityMapper::mapToTask)
                .toList())
        .build();
  }

  private static Status mapToStatus(final StatusEntity input) {
    return Status.builder().title(input.getTitle()).color(input.getColor()).build();
  }

  private static Priority mapToPriority(final PriorityEntity input) {
    return Priority.builder().title(input.getTitle()).color(input.getColor()).build();
  }

  private static LocalDateTime getUserCreatedAt(final User user) {
    return user.getCreatedAt() == null ? LocalDateTime.now() : user.getCreatedAt();
  }
}
