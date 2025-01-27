package org.example.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOutput {

  @Schema(
      description = "username",
      implementation = String.class,
      examples = {"johnny123"})
  private String username;

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
      description = "session token for a user",
      implementation = String.class
  )
  private String token;


  private List<Task> taskList;
  private List<Message> errorList;
}
