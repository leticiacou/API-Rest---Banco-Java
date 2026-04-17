package br.com.leticiacouto.ProjetoBanco.database.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
}
