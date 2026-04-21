package br.com.leticiacouto.ProjetoBanco.service;

import br.com.leticiacouto.ProjetoBanco.database.model.User;
import br.com.leticiacouto.ProjetoBanco.database.repository.Database;
import br.com.leticiacouto.ProjetoBanco.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
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
            throw new Error("Erro ao criar o usuário, senha fraca");
        }else if(!userdto.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>\\-_+=\\[\\]/\\\\'`~].*")){
            throw new Error("Erro ao criar o usuário, a senha precisa conter pelo menos um caractere especial");
        }else if(userdto.getEmail().length() < 8){
            throw new Error("Erro ao criar o usuário, email inválido");
        }else if(userdto.getName().length() < 4){
            throw new Error("Erro ao criar o usuário, nome inválido");
        }

        database.getUsers().add(newUser);
        return newUser;
    }

    public Set<User> getAllUsers(){
        if(database.getUsers().isEmpty()){
            throw new Error("Não existem usuários no banco de dados");
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
            throw new Error("Usuário não encontrado");
        }else{
            return user;
        }
    }

    public User updateUser(UserDto userdto, int id) {
        User usuario = getUserById(id);

        usuario.setName(userdto.getName());
        usuario.setEmail(userdto.getEmail());
        usuario.setPassword(userdto.getPassword());

        return usuario;
    }
}
