package br.com.trabalhofinal.fabrica_software.config;

import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("\n=== Iniciando processo de autenticação ===");
        System.out.println("Tentando autenticar usuário: " + username);
        
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    System.out.println("Usuário não encontrado: " + username);
                    return new UsernameNotFoundException("Usuário não encontrado: " + username);
                });

        System.out.println("Usuário encontrado: " + user.getEmail());
        System.out.println("Senha hash armazenada: " + user.getPassword());
        System.out.println("Roles: " + user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(", ")));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        System.out.println("CustomUserDetails criado com sucesso");
        System.out.println("Username no CustomUserDetails: " + userDetails.getUsername());
        System.out.println("Password hash no CustomUserDetails: " + userDetails.getPassword());
        System.out.println("Authorities: " + userDetails.getAuthorities());
        System.out.println("=== Fim do processo de carregamento do usuário ===\n");

        return userDetails;
    }
} 