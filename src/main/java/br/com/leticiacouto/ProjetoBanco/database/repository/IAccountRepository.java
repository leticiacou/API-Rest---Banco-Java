package br.com.leticiacouto.ProjetoBanco.database.repository;

import br.com.leticiacouto.ProjetoBanco.database.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IAccountRepository extends JpaRepository<AccountEntity, UUID> {
}
