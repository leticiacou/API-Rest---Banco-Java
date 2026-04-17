package br.com.leticiacouto.ProjetoBanco.controller;

import br.com.leticiacouto.ProjetoBanco.database.model.User;
import br.com.leticiacouto.ProjetoBanco.dto.UserDto;
import br.com.leticiacouto.ProjetoBanco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User userPost(@RequestBody UserDto userdto){
        return userService.createUser(userdto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<User> userGet(){
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public User userGet2(@PathVariable int id){
        return userService.getUserById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User userPut(@RequestBody UserDto userdto,@PathVariable int id){
        return userService.updateUser(userdto, id);
    }
}
