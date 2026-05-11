package br.com.leticiacouto.ProjetoBanco.service;

import br.com.leticiacouto.ProjetoBanco.database.model.AccountEntity;
import br.com.leticiacouto.ProjetoBanco.database.model.TransactionEntity;
import br.com.leticiacouto.ProjetoBanco.database.model.UserEntity;
import br.com.leticiacouto.ProjetoBanco.database.repository.IAccountRepository;
import br.com.leticiacouto.ProjetoBanco.database.repository.ITransactionRepository;
import br.com.leticiacouto.ProjetoBanco.database.repository.IUserRepository;
import br.com.leticiacouto.ProjetoBanco.dto.TransferDto;
import br.com.leticiacouto.ProjetoBanco.exceptions.BusinessException;
import br.com.leticiacouto.ProjetoBanco.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
//    ACIONAR MAIS ALGUMAS REGRAS DE NEGÓCIO

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;
    private final ITransactionRepository transactionRepository;

    public AccountService(IAccountRepository accountRepository, IUserRepository userRepository,  ITransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
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

        TransactionEntity newTransaction = TransactionEntity.builder()
                .amount(amount)
                .type("deposit")
                .account(account)
                .build();

        transactionRepository.save(newTransaction);
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

        TransactionEntity newTransaction = TransactionEntity.builder()
                .amount(amount)
                .type("withdraw")
                .account(account)
                .build();

        transactionRepository.save(newTransaction);

        return accountRepository.save(account).getBalance();
    }


    public String transferMoney(TransferDto transferDto) {
        UUID account1ID = transferDto.getAccountFrom();
        UUID account2ID = transferDto.getAccountTo();

        AccountEntity accountFrom = accountRepository.findById(account1ID)
                .orElseThrow(() -> new ResourceNotFoundException("Conta origem não encontrada"));
        AccountEntity accountTo = accountRepository.findById(account2ID)
                .orElseThrow(() -> new ResourceNotFoundException("Conta de destino não encontrada"));

        if(accountFrom == null || accountTo == null) {
            throw new ResourceNotFoundException("Conta não encontrada");
        }else if(accountFrom.getBalance() < transferDto.getAmount()) {
            throw  new BusinessException("Saldo insuficiente");
        }

        var newBalance1 = accountFrom.getBalance() - transferDto.getAmount();
        accountFrom.setBalance(newBalance1);
        var newBalance2 = accountTo.getBalance() + transferDto.getAmount();
        accountTo.setBalance(newBalance2);

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);

        TransactionEntity newTransaction = TransactionEntity.builder()
                .amount(transferDto.getAmount())
                .type("transfer")
                .account(accountFrom)
                .build();

        transactionRepository.save(newTransaction);

        return "O saldo atualizado após a transferencia é " + newBalance1;
    }

    public List<TransactionEntity> getTransactions(UUID id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));

        return transactionRepository.findAllByAccount(account);
    }
}
