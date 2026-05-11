package br.com.leticiacouto.ProjetoBanco.service;

import br.com.leticiacouto.ProjetoBanco.database.model.UserEntity;
import br.com.leticiacouto.ProjetoBanco.database.repository.IUserRepository;
import br.com.leticiacouto.ProjetoBanco.dto.UserDto;
import br.com.leticiacouto.ProjetoBanco.exceptions.BusinessException;
import br.com.leticiacouto.ProjetoBanco.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserDto userDto) {
//      REGRA DE UM NAO DUPLICAR USUARIO(POR EMAIL)
//      CRIPTOGRAFAR A SENHA

        UserEntity newUser = UserEntity.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        if(userDto.getPassword().length() < 8){
            throw new BusinessException("Erro ao criar a senha, insira no mínimo 8 caracteres");
        }else if(!userDto.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>\\-_+=\\[\\]/\\\\'`~].*")){
            throw new BusinessException("Erro ao criar o usuário, a senha precisa conter pelo menos um caractere especial");
        }else if(userDto.getEmail().length() < 8){
            throw new BusinessException("Erro ao criar o usuário, email inválido");
        }else if(userDto.getName().length() < 4){
            throw new BusinessException("Erro ao criar o usuário, nome inválido");
        }

        return userRepository.save(newUser);
    }

    public List<UserEntity> findAll(){
        if(userRepository.findAll().isEmpty()) {
            throw new RuntimeException("Nenhum user encontrado");
        }
        return userRepository.findAll();
    }

    public UserEntity getUserById(UUID id){
        if(userRepository.findById(id) == null) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        return userRepository.getById(id);
    }

    public UserEntity updateUser(UserDto userdto, UUID id) {
//      erros já setados na função getUserByid
        UserEntity usuario = getUserById(id);

        usuario.setName(userdto.getName());
        usuario.setEmail(userdto.getEmail());
        usuario.setPassword(userdto.getPassword());

        return userRepository.save(usuario);
    }
}
