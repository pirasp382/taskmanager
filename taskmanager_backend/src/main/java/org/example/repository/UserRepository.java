package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.Map;
import org.example.dto.database.UserEntity;

public class UserRepository implements PanacheRepository<UserEntity> {

  public UserEntity findByName(final String username) {
    return find("username", username).firstResult();
  }

  public UserEntity findByUserID(final Long id){
    return find("id", id).firstResult();
  }

  public UserEntity findByMail(final String email) {
    return find("email", email).firstResult();
  }

  public UserEntity findByUsernameAndPassword(final String username, final String password) {
    return find(
            "username =: usernameParam and password =: passwordParam",
            Map.of("usernameParam", username, "passwordParam", password))
        .firstResult();
  }
}
