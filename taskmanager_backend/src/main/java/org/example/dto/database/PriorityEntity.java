package org.example.dto.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "priority")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriorityEntity extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "color")
  private String color;

  @ManyToMany(mappedBy = "priorities", fetch = FetchType.LAZY)
  private Set<UserEntity> users = new HashSet<>();
}
