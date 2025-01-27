package org.example.mapper;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Logger;

import lombok.experimental.UtilityClass;
import org.example.dto.Priority;
import org.example.dto.Status;
import org.example.dto.Task;
import org.example.dto.database.PriorityEntity;
import org.example.dto.database.StatusEntity;
import org.example.dto.database.TaskEntity;
import org.example.dto.database.UserEntity;

@UtilityClass
public class TaskEntityMapper {

  public static TaskEntity mapToTaskEntity(final Task task, final UserEntity user) {

    return TaskEntity.builder()
        .title(task.getTitle())
        .description(task.getDescription())
        .lastUpdate(LocalDateTime.now())
        .createdDate(task.getCreatedDate() == null ? LocalDateTime.now() : task.getCreatedDate())
        .status(getStatusEntity(task.getStatus()))
        .priority(getPriorityEntity(task.getPriority()))
        .user(user)
        .build();
  }

  public static StatusEntity getStatusEntity(final Status status) {
    if (status == null || status.getTitle() == null) {
      return StatusEntity.find(
              "select s from StatusEntity s where s.title =: titleParam",
              Map.of("titleParam", "todo"))
          .firstResult();
    }
    final StatusEntity statusEntity =
        StatusEntity.find(
                "select s from StatusEntity s where s.title =: titleParam",
                Map.of("titleParam", status.getTitle()))
            .firstResult();
    if (statusEntity == null) {
      return StatusEntity.find(
              "select s from StatusEntity s where s.title =: titleParam",
              Map.of("titleParam", "todo"))
          .firstResult();
    }
    return statusEntity;
  }

  public static PriorityEntity getPriorityEntity(final Priority priority) {
    if (priority == null || priority.getTitle() == null) {
      return PriorityEntity.find(
              "select p from PriorityEntity p where p.title =: titleParam",
              Map.of("titleParam", "low"))
          .firstResult();
    }
    final PriorityEntity priorityEntity =
        PriorityEntity.find(
                "select p from PriorityEntity p where p.title =: titleParam",
                Map.of("titleParam", priority.getTitle()))
            .firstResult();
    if (priorityEntity == null) {
      return PriorityEntity.find(
              "select p from PriorityEntity p where p.title =: titleParam",
              Map.of("titleParam", "low"))
          .firstResult();
    }
    return priorityEntity;
  }

  private Status mapToStatus(final StatusEntity statusEntity) {
    return Status.builder().title(statusEntity.getTitle()).color(statusEntity.getColor()).build();
  }

  private Priority mapTPriority(final PriorityEntity priorityEntity) {
    return Priority.builder()
        .title(priorityEntity.getTitle())
        .color(priorityEntity.getColor())
        .build();
  }

  public static Task mapToTask(final TaskEntity task) {
    return Task.builder()
        .id(task.getId())
        .title(task.getTitle())
        .status(mapToStatus(task.getStatus()))
        .priority(mapTPriority(task.getPriority()))
        .createdDate(task.getCreatedDate())
        .lastUpdate(task.getLastUpdate())
        .description(task.getDescription())
        .userid(task.getUser().getId())
        .build();
  }
}
