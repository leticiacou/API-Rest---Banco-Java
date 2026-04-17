package br.com.leticiacouto.ProjetoBanco.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDto {
    private UUID accountFrom;
    private UUID accountTo;
    private Double amount;
}
