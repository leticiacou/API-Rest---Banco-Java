package br.com.leticiacouto.ProjetoBanco.controller;

import br.com.leticiacouto.ProjetoBanco.database.model.UserEntity;
import br.com.leticiacouto.ProjetoBanco.dto.DeleteUserDto;
import br.com.leticiacouto.ProjetoBanco.dto.UserDto;
import br.com.leticiacouto.ProjetoBanco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity userPost(@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntity> userGet(){
        return userService.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity userGet2(@PathVariable UUID id){
        return userService.getUserById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity userPut(@RequestBody UserDto userdto,@PathVariable UUID id){
        return userService.updateUser(userdto, id);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String userDelete(@RequestBody DeleteUserDto deleteUserDto){
        return userService.deleteUser(deleteUserDto);
    }
}
