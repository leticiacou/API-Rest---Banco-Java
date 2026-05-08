package br.com.leticiacouto.ProjetoBanco.service;

import br.com.leticiacouto.ProjetoBanco.database.model.AccountEntity;
import br.com.leticiacouto.ProjetoBanco.database.model.UserEntity;
import br.com.leticiacouto.ProjetoBanco.database.repository.IAccountRepository;
import br.com.leticiacouto.ProjetoBanco.database.repository.IUserRepository;
import br.com.leticiacouto.ProjetoBanco.exceptions.BusinessException;
import br.com.leticiacouto.ProjetoBanco.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {
//    ACIONAR MAIS ALGUMAS REGRAS DE NEGÓCIO

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;

    public AccountService(IAccountRepository accountRepository, IUserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountEntity createAccount(UUID userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        AccountEntity newAccount = AccountEntity.builder()
                .user(userEntity)
                .balance(0)
                .build();

        return accountRepository.save(newAccount);
    }

    public Double getBalence(UUID id) {
        AccountEntity account = accountRepository.getById(id);

        if(account == null) {
            throw new ResourceNotFoundException("Essa conta não existe");
        }

        return account.getBalance();
    }

    public Double deposit(UUID id, Double amount) {
        Double newBalance;
        AccountEntity account = accountRepository.getById(id);

        if(amount < 0){
            throw new BusinessException("Valor inválido");
        }else{
            newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);
        }

        return accountRepository.save(account).getBalance();
    }

    public Double withdraw(UUID id, Double amount) {
        Double newBalance;
        AccountEntity account = accountRepository.getById(id);

        if(amount < 0){
            throw new BusinessException("Valor inválido");
        }else if(amount > account.getBalance()){
            throw new BusinessException("Valor inválido, saldo insuficiente");
        }else{
            newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
        }

        return accountRepository.save(account).getBalance();
    }


    //    FAZER ESSA FUNCAO FUNCIONAR
//    public String transferMoney(TransferDto transferDto) {
//        var account1 = transferDto.getAccountFrom();
//        var account2 = transferDto.getAccountTo();
//
//        if(account1.() < transferDto.getAmount()){
//            throw  new BusinessException("Saldo insuficiente");
//        }
//
//        var newBalance1 = account1.getBalance() - transferDto.getAmount();
//        account1.setBalance(newBalance1);
//        var newBalance2 = account2.getBalance() + transferDto.getAmount();
//        account2.setBalance(newBalance2);
//
//        return "O saldo atualizado após a transferencia é " + newBalance1;
//    }
}
