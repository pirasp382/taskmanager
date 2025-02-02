package org.example.services.validation;

import lombok.experimental.UtilityClass;
import org.example.repository.UserRepository;


@UtilityClass
public class EditUserValidation {

  final UserRepository userRepository = new UserRepository();

  public boolean usernameIsUnique(final String username) {
    return userRepository.findByName(username) == null;
  }

  public boolean emailIsUnique(final String email){
    return userRepository.findByMail(email) == null;
  }
}
