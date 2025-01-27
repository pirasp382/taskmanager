package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.example.dto.database.PriorityEntity;
import org.example.dto.database.UserEntity;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PriorityRepository implements PanacheRepository<PriorityEntity> {

  public List<PriorityEntity> getInitalPriorityItems() {
    return find("title = 'low' or title = 'middle' or title = 'high'").list();
  }

  @Transactional
  public List<PriorityEntity> getAll(final UserEntity user){
    return find("SELECT p from PriorityEntity p join p.users u where u =: userParam",
            Map.of("userParam", user))
            .list();
  }
}
