package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.dto.TasksDetail;
import org.example.dto.database.TaskEntity;
import org.example.dto.database.UserEntity;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<TaskEntity> {

  @Transactional
  public List<TaskEntity> getAllTasksByUserId(final UserEntity user) {
    return find("select t from TaskEntity t where t.user =: userId", Map.of("userId", user)).list();
  }

  @Transactional
  public List<TaskEntity> getTaskByIdAndUserId(final long id, final UserEntity user) {
    return find(
            "select t from TaskEntity t where t.user =: userId and t.id =: id",
            Map.of("userId", user, "id", id))
        .list();
  }

  @Transactional
  public long deleteById(final long id, final UserEntity user) {
    return delete(
        "delete from TaskEntity t where t.id =: idParam and t.user =: userParam",
        Map.of("idParam", id, "userParam", user));
  }

  @Transactional
  public List<TaskEntity> getSortedTaskList(
      final UserEntity user, final String sortParam, final boolean direction) {
    return find(
            "select t from TaskEntity t where t.user =: userParam",
            Sort.by(sortParam, getSortDirection(direction)),
            Map.of("userParam", user))
        .list();
  }

  private Sort.Direction getSortDirection(final boolean direction) {
    return direction ? Sort.Direction.Ascending : Sort.Direction.Descending;
  }

  @Transactional
  public List<TasksDetail> getTasksCountByStatus(final UserEntity user) {
    return TaskEntity.getEntityManager()
        .createQuery(
            "select new org.example.dto.TasksDetail(s.title, s.color, count(t)) from TaskEntity t "
                + "join StatusEntity s on s.id = t.status.id "
                + "where t.user =: userParam "
                + "group by s")
        .setParameter("userParam", user)
        .getResultList();
  }

  @Transactional
  public List<TasksDetail> getTasksCountByPriority(final UserEntity user) {
    return TaskEntity.getEntityManager()
        .createQuery(
            "select  new org.example.dto.TasksDetail(p.title, p.color, count(t)) from TaskEntity t "
                + "join PriorityEntity p on p.id = t.priority.id "
                + "where t.user =: userParam "
                + "group by p")
        .setParameter("userParam", user)
        .getResultList();
  }
}
