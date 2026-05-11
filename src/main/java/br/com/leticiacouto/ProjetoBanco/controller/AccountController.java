package br.com.leticiacouto.ProjetoBanco.controller;

import br.com.leticiacouto.ProjetoBanco.database.model.AccountEntity;
import br.com.leticiacouto.ProjetoBanco.database.model.TransactionEntity;
import br.com.leticiacouto.ProjetoBanco.dto.TransferDto;
import br.com.leticiacouto.ProjetoBanco.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountEntity> createAccount(@PathVariable UUID userId) {
        AccountEntity account = accountService.createAccount(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
        public Double getBalance(@PathVariable UUID id) {
        return accountService.getBalence(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Double setBalance(@PathVariable UUID id, @RequestBody Double amount) {
        return accountService.deposit(id, amount);
    }

    @PutMapping("withdraw/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Double withdraw(@PathVariable UUID id, @RequestBody Double amount) {
        return accountService.withdraw(id, amount);
    }

    @PutMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public String transferMoney(@RequestBody TransferDto transferDto) {
        System.out.println(transferDto);
        return accountService.transferMoney(transferDto);
    }

    @GetMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionEntity> getTransactions(@PathVariable UUID id) {
        return accountService.getTransactions(id);
    }
}
