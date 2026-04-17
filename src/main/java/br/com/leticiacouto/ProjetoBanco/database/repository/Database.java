package br.com.leticiacouto.ProjetoBanco.database.repository;

import java.util.HashSet;
import java.util.Set;
import br.com.leticiacouto.ProjetoBanco.database.model.User;
import br.com.leticiacouto.ProjetoBanco.database.model.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Database {
    private Set<User> users = new HashSet<>();
    private Set<Account> accounts = new HashSet<>();
}
