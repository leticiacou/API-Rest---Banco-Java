package br.com.leticiacouto.ProjetoBanco.database.repository;

import br.com.leticiacouto.ProjetoBanco.database.model.AccountEntity;
import br.com.leticiacouto.ProjetoBanco.database.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ITransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    public List<TransactionEntity> findAllByAccount(AccountEntity account);
}
