package br.com.leticiacouto.ProjetoBanco.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "transactions")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",  nullable = false,  updatable = false, insertable = false)
    private UUID Id;

    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private AccountEntity account;

    @Column(name = "type", nullable = false, updatable = false)
    private String type;

    @Column(name = "amount",  nullable = false, updatable = false)
    private Double amount;
}
