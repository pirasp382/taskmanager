package org.example.services.validation;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.example.dto.Message;
import org.example.dto.User;
import org.example.repository.UserRepository;

@UtilityClass
public class RegistrationValidation {

  final UserRepository userRepository = new UserRepository();

  public static List<Message> validate(final User user) {
    final List<Message> errorlist = new ArrayList<>();
    if (inputIsNull(user.getUsername())) {
      errorlist.add(Message.builder().title("Username is null").build());
    } else if (usernameIsNotUnique(user.getUsername())) {
      errorlist.add(Message.builder().title("Username is not unique").build());
    }
    if (inputIsNull(user.getEmail())) {
      errorlist.add(Message.builder().title("EMail Address is null").build());
    } else if (emailAddressIsNotUnique(user.getEmail())) {
      errorlist.add(Message.builder().title("EMail Address is not unique").build());
    }
    if (inputIsNull(user.getPassword())) {
      errorlist.add(Message.builder().title("Password is null").build());
    }
    return errorlist;
  }

  private static boolean emailAddressIsNotUnique(final String email) {
    return userRepository.findByMail(email) != null;
  }

  private static boolean inputIsNull(final String input) {
    return input == null || input.isEmpty();
  }

  private static boolean usernameIsNotUnique(final String username) {
    return userRepository.findByName(username) != null;
  }
}
