package org.example.services.validation;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.example.dto.LoginInput;
import org.example.dto.Message;
import org.example.repository.UserRepository;
import org.example.util.Util;

@UtilityClass
public class LoginValidation {

  private final UserRepository userRepository = new UserRepository();

  public List<Message> validate(final LoginInput input) {
    final List<Message> errorList = new ArrayList<>();
    if (isNullOrEmpty(input.getUsername())) {
      errorList.add(Message.builder().title("username is null").build());
    }
    if (isNullOrEmpty(input.getPassword())) {
      errorList.add(Message.builder().title("password is null").build());
    }
    if(userDoesNotExists(input.getUsername())){
      errorList.add(Message.builder().title("user does not exists").build());
    }
    if (errorList.isEmpty() && wrongCredentials(input.getUsername(), input.getPassword())) {
      errorList.add(Message.builder().title("wrong credentials").build());
    }

    return errorList;
  }

  private static boolean userDoesNotExists(final String username){
    return userRepository.findByName(username) == null;
  }

  private static boolean isNullOrEmpty(final String input) {
    return input == null || input.isEmpty();
  }

  private static boolean wrongCredentials(final String username, final String password) {
    final var user = userRepository.findByName(username);
    return !Util.validatePassword(password, user.getPassword());
  }
}
