package org.example.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;
import org.example.dto.Message;
import org.example.dto.User;
import org.example.repository.UserRepository;
import org.example.util.Util;

@UtilityClass
public class RegistrationValidation {

  final UserRepository userRepository = new UserRepository();

  public static List<Message> validate(final User user) {
    final List<Message> errorlist = new ArrayList<>();
    if (inputIsNull(user.getUsername())) {
      errorlist.add(Message.builder().title("Username is null").build());
    } else if (usernameIsNotUnique(user.getUsername())) {
      errorlist.add(Message.builder().title("Username is not unique").build());
    } else if (usernameIsTooShort(user.getUsername())) {
      errorlist.add(Message.builder().title("Username is too short").build());
    }
    if (inputIsNull(user.getEmail())) {
      errorlist.add(Message.builder().title("EMail Address is null").build());
    } else if (emailAddressIsNotUnique(user.getEmail())) {
      errorlist.add(Message.builder().title("EMail Address is not unique").build());
    } else if (validateEMailAddressFormat(user.getEmail())) {
      errorlist.add(Message.builder().title("\"Email-Address is not valid").build());
    }
    if (inputIsNull(user.getPassword())) {
      errorlist.add(Message.builder().title("Password is null").build());
    } else if (passwordHasWrongFormat(user.getPassword())) {
      errorlist.add(Message.builder().title("Password has worong format").build());
    }
    return errorlist;
  }

  private static boolean emailAddressIsNotUnique(final String email) {
    final String hashedPassword = Util.hashValue(email);
    return userRepository.findByMail(hashedPassword) != null;
  }

  private static boolean inputIsNull(final String input) {
    return input == null || input.isEmpty();
  }

  private static boolean usernameIsNotUnique(final String username) {
    return userRepository.findByName(username) != null;
  }

  private static boolean usernameIsTooShort(final String username) {
    return username.length() < 5;
  }

  private static boolean passwordHasWrongFormat(final String password) {
    final Pattern uppercase = Pattern.compile("[A-Z]+");
    final Pattern lowercase = Pattern.compile("[a-z]+");
    final Pattern digit = Pattern.compile("\\d+");
    final Pattern specialCharacter = Pattern.compile("[^\\w\\s]+");
    final Matcher lowercaseMatcher = lowercase.matcher(password);
    final Matcher uppercaseMatcher = uppercase.matcher(password);
    final Matcher digitMatcher = digit.matcher(password);
    final Matcher spechialMatcher = specialCharacter.matcher(password);
    return !(lowercaseMatcher.find()
        && uppercaseMatcher.find()
        && digitMatcher.find()
        && spechialMatcher.find()
        && password.length() >= 6);
  }

  private static boolean validateEMailAddressFormat(final String email) {
    final String pattern =
        "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    final Pattern emailPattern = Pattern.compile(pattern);
    final Matcher matcher = emailPattern.matcher(email);
    return !matcher.find();
  }
}
