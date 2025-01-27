package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.example.dto.database.StatusEntity;
import org.example.dto.database.UserEntity;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class StatusRepository implements PanacheRepository<StatusEntity> {

  @Transactional
  public List<StatusEntity> getInitalStatusItems() {
    return find("title = 'todo' or title = 'in_progress' or title = 'done'").list();
  }

  @Transactional
  public List<StatusEntity> getAll(final UserEntity user) {
    return find(
            "SELECT s FROM StatusEntity s JOIN s.users u WHERE u = :userParam",
            Map.of("userParam", user))
        .list();
  }
}
