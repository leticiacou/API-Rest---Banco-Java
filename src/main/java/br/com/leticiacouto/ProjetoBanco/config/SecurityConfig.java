package br.com.leticiacouto.ProjetoBanco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//desativa a permissão para tudo, se eu quiser que algo especifico tenha eu coloco no método
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//        CSRF (Cross-Site Request Forgery) é um tipo de ataque onde um site malicioso faz requisições no lugar do usuário sem ele saber.
//        O Spring Security ativa a proteção CSRF por padrão — mas ela é feita para aplicações com tela/formulário HTML (como um site tradicional)
                .csrf(csrf -> csrf.disable()) // desativa CSRF (comum em APIs REST)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // ✅ libera todas as rotas
                );

        return http.build();
    }

    // Spring guarda esse objeto e injeta onde precisar, devido à anotação bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}