package br.com.leticiacouto.ProjetoBanco.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserDto {
    private UUID id;
    private String password;
    private String confirmPassword;
}

