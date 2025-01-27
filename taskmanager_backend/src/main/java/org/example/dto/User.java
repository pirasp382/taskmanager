package org.example.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Schema(
      description = "username",
      implementation = String.class,
      examples = {"johnny123"})
  private String username;

  @Schema(
      description = "user's password",
      implementation = String.class,
      examples = {"helloWorld123"})
  private String password;

  @Schema(
          description = "confirmed password",
          implementation = String.class,
          examples = {"helloWorld123"})
  private String confirmPasswod;

  @Schema(
      description = "user's fullname",
      implementation = String.class,
      examples = {"John Smith"})
  private String fullname;

  @Schema(
      description = "user's bio",
      implementation = String.class,
      examples = {"hello world 123"})
  private String bio;

  @Schema(description = "avatar url to user profile picture", implementation = String.class)
  private String avatarUrl;

  @Schema(
      description = "locateDate to last login",
      implementation = LocalDateTime.class,
      examples = {"2025-01-08 14:46:29"})
  @Builder.Default
  private LocalDateTime lastLogin = LocalDateTime.now();

  @Schema(
      description = "localdate of creation date",
      implementation = LocalDateTime.class,
      examples = {"2025-01-08 14:46:29"})
  private LocalDateTime createdAt;

  @Schema(
      description = "user email address",
      implementation = String.class,
      examples = {"johnnymail.com"})
  private String email;
}
