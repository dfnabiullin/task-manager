package io.github.dfnabiullin.taskmanager.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @PrePersist
    public void onBeforeCreate() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}