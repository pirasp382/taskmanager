package org.example.dto.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusEntity extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "color")
  private String color;

  @ManyToMany(mappedBy = "statuses")
  private Set<UserEntity> users = new HashSet<>();

  @OneToMany(mappedBy = "status")
  private Set<TaskEntity> tasks = new HashSet<>();
}
