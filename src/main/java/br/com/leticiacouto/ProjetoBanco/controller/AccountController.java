package br.com.leticiacouto.ProjetoBanco.controller;

import br.com.leticiacouto.ProjetoBanco.database.model.Account;
import br.com.leticiacouto.ProjetoBanco.dto.TransferDto;
import br.com.leticiacouto.ProjetoBanco.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    AccountService accountService = new AccountService();

    @PostMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@PathVariable int id) {
        return accountService.createAccount(id);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
        public Double getBalance(@PathVariable UUID id) {
        return accountService.getBalence(id);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Double setBalance(@PathVariable UUID id, @RequestBody Double amount) {
        return accountService.deposit(id, amount);
    }

    @PatchMapping("withdraw/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Double withdraw(@PathVariable UUID id, @RequestBody Double amount) {
        return accountService.withdraw(id, amount);
    }

    @PatchMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public String transferMoney(@RequestBody TransferDto transferDto) {
        return accountService.transferMoney(transferDto);
    }
}
