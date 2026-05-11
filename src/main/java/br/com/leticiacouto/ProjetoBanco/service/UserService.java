package br.com.leticiacouto.ProjetoBanco.service;

import br.com.leticiacouto.ProjetoBanco.database.model.AccountEntity;
import br.com.leticiacouto.ProjetoBanco.database.model.UserEntity;
import br.com.leticiacouto.ProjetoBanco.database.repository.IAccountRepository;
import br.com.leticiacouto.ProjetoBanco.database.repository.IUserRepository;
import br.com.leticiacouto.ProjetoBanco.dto.DeleteUserDto;
import br.com.leticiacouto.ProjetoBanco.dto.UserDto;
import br.com.leticiacouto.ProjetoBanco.exceptions.BusinessException;
import br.com.leticiacouto.ProjetoBanco.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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
    private final IAccountRepository accountRepository;

    public UserService(IUserRepository userRepository,  IAccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public UserEntity createUser(UserDto userDto) {

        UserEntity newUser = UserEntity.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

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

    @Transactional
    public String deleteUser(DeleteUserDto deleteUserDto) {
        UserEntity usuario = userRepository.findById(deleteUserDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if(!passwordEncoder.matches(deleteUserDto.getPassword(), usuario.getPassword())) {
            throw new BusinessException("Senha incorreta");
        } else if (!deleteUserDto.getPassword().equals(deleteUserDto.getConfirmPassword())) {
            throw new BusinessException("As senhas não coincidem");
        }

        AccountEntity account = accountRepository.findByUserId(deleteUserDto.getId());

        userRepository.deleteById(deleteUserDto.getId());
        accountRepository.deleteById(account.getId());

        return "Usuário e conta associada deletados com sucesso!";
    }
}
