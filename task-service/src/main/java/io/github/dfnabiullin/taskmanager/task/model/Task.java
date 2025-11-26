package io.github.dfnabiullin.taskmanager.task.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;
    private UUID assigneeUuid;
    @Column(length = 1000)
    private String description;

    @PrePersist
    public void onBeforeCreate() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}