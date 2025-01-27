package org.example.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

  private long id;
  private String title;
  private String description;
  private LocalDateTime createdDate;
  private LocalDateTime lastUpdate;
  private Status status;
  private Priority priority;
  private long userid;
}
