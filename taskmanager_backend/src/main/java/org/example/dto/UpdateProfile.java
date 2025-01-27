package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfile {
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
      description = "user email address",
      implementation = String.class,
      examples = {"johnnymail.com"})
  private String email;
}
