package br.com.leticiacouto.ProjetoBanco.service;

import br.com.leticiacouto.ProjetoBanco.database.model.Account;
import br.com.leticiacouto.ProjetoBanco.database.repository.Database;
import br.com.leticiacouto.ProjetoBanco.dto.TransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private Database database;

    public Account createAccount(int id) {

        Account newAccount = Account.builder()
                .id(UUID.randomUUID())
                .userId(id)
                .balance(0)
                .build();

        boolean idExist = database.getAccounts().stream()
                .anyMatch(acc -> acc.getId().equals(newAccount.getId()));
        if (!idExist) {
            database.getAccounts().add(newAccount);
        }else{
            throw new RuntimeException("A conta já existe");
        }

        boolean userExist = database.getUsers().stream()
                .anyMatch(user -> user.getId() == newAccount.getUserId());
        if(!userExist){
            throw new Error("O usuário não foi encontrado");
        }

        boolean moreThanOne = database.getAccounts().stream()
                .filter(acc -> acc.getUserId() == newAccount.getUserId())
                .count() > 1;
        if (moreThanOne) {
            throw new Error("O usuário já tem uma conta ativa");
        }

        return newAccount;
    }

    public Double getBalence(UUID id) {
        var account = database.getAccounts().stream()
                .filter(acc -> acc.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        return account.getBalance();
    }

    public Double deposit(UUID id, Double amount) {
        Double newBalance;
        Account account = database.getAccounts().stream()
                .filter(acc -> acc.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if(amount < 0){
            throw new Error("Valor inválido");
        }else{
            newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);
        }

        return newBalance;
    }

    public Double withdraw(UUID id, Double amount) {
        Double newBalance;

        Account account = database.getAccounts().stream()
                .filter(acc -> acc.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if(amount < 0){
            throw new Error("Valor inválido");
        }else if(amount > account.getBalance()){
            throw new Error("Valor inválido");
        }else{
            newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
        }



        return newBalance;
    }

    public String transferMoney(TransferDto transferDto) {
        var account1 = database.getAccounts().stream()
                .filter(acc -> acc.getId().equals(transferDto.getAccountFrom()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        var account2 = database.getAccounts().stream()
                .filter(acc -> acc.getId().equals(transferDto.getAccountTo()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if(account1.getBalance() < transferDto.getAmount()){
            throw  new Error("Saldo insuficiente");
        }

        var newBalance1 = account1.getBalance() - transferDto.getAmount();
        account1.setBalance(newBalance1);
        var newBalance2 = account2.getBalance() + transferDto.getAmount();
        account2.setBalance(newBalance2);

        return "O saldo atualizado após a transferencia é " + newBalance1;
    }
}
