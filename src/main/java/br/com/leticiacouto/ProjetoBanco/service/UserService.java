package br.com.leticiacouto.ProjetoBanco.service;

import br.com.leticiacouto.ProjetoBanco.database.model.User;
import br.com.leticiacouto.ProjetoBanco.database.repository.Database;
import br.com.leticiacouto.ProjetoBanco.dto.UserDto;
import br.com.leticiacouto.ProjetoBanco.exceptions.BusinessException;
import br.com.leticiacouto.ProjetoBanco.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private Database database;

    public User createUser(UserDto userdto) {
        int identificador = database.getUsers().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;

        User newUser = User.builder()
                .id(identificador)
                .name(userdto.getName())
                .email(userdto.getEmail())
                .password(userdto.getPassword())
                .build();

        if(userdto.getPassword().length() < 8){
            throw new BusinessException("Erro ao criar a senha, insira no mínimo 8 caracteres");
        }else if(!userdto.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>\\-_+=\\[\\]/\\\\'`~].*")){
            throw new BusinessException("Erro ao criar o usuário, a senha precisa conter pelo menos um caractere especial");
        }else if(userdto.getEmail().length() < 8){
            throw new BusinessException("Erro ao criar o usuário, email inválido");
        }else if(userdto.getName().length() < 4){
            throw new BusinessException("Erro ao criar o usuário, nome inválido");
        }

        database.getUsers().add(newUser);
        return newUser;
    }

    public Set<User> getAllUsers(){
        if(database.getUsers().isEmpty()){
            throw new ResourceNotFoundException("Não há usuários no sistema");
        }else{
            return database.getUsers();
        }
    }

    public User getUserById(int id){
        User user = database.getUsers().stream()
                .filter(u -> u.getId() == id)
                .findAny()
                .orElse(null);

        if(user == null){
            throw new ResourceNotFoundException("Usuário não encontrado");
        }else{
            return user;
        }
    }

    public User updateUser(UserDto userdto, int id) {
//      erros já setados na função getUserByid
        User usuario = getUserById(id);

        usuario.setName(userdto.getName());
        usuario.setEmail(userdto.getEmail());
        usuario.setPassword(userdto.getPassword());

        return usuario;
    }
}
