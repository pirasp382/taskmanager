package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginInput {
  @Schema(
          description = "username",
          implementation = String.class,
          examples = {"johnny123"}
  )
  private String username;

  @Schema(
          description = "password",
          implementation = String.class,
          examples = {"hello_world_123"}
  )
  private String password;
}
