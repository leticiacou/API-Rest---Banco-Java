package br.com.leticiacouto.ProjetoBanco.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false,  nullable = false)
    private UUID id;

    @Column(name = "name",  nullable = false)
    private String name;

    @Column(name = "email",   nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
}
