package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegistrationOutput {

  private String username;
  private String token;

  @Builder.Default
  private List<Message> errorlist=List.of();
}
