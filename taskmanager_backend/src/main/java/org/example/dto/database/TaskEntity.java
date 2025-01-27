package org.example.dto.database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.example.dto.Status;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "task title", implementation = String.class)
  private long id;

  @Column(name = "title", nullable = false)
  @Schema(description = "task title", implementation = String.class)
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "lastUpdate", nullable = false)
  private LocalDateTime lastUpdate;

  @Column(name = "createdDate", nullable = false)
  private LocalDateTime createdDate;

  @ManyToOne
  @JoinColumn(name = "status_id", nullable = false)
  @JsonBackReference
  private StatusEntity status;

  @ManyToOne
  @JoinColumn(name = "priorityId", nullable = false)
  @JsonBackReference
  private PriorityEntity priority;

  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  @JsonBackReference
  private UserEntity user;
}
