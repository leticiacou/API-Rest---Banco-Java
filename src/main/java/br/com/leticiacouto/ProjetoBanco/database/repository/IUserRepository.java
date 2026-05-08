package br.com.leticiacouto.ProjetoBanco.database.repository;

import br.com.leticiacouto.ProjetoBanco.database.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//não precisa da anotação, só de extender o jpa já se entende como um repository
@Repository
//ou cruderepositoy, mas o jpa tem mais
public interface IUserRepository extends JpaRepository<UserEntity, UUID> {
}
