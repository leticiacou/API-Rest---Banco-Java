package br.com.leticiacouto.ProjetoBanco.database.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    private UUID id;
    private int userId;
    private double balance;

}
