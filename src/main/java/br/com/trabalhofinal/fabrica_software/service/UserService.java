package br.com.trabalhofinal.fabrica_software.service;

import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.model.Role;
import br.com.trabalhofinal.fabrica_software.repository.UserRepository;
import br.com.trabalhofinal.fabrica_software.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
Serviço para operações relacionadas a usuários.
*/
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
    Busca todos os usuários
    @return Lista de usuários
    */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
    Busca um usuário pelo ID
    @param id ID do usuário
    @return Optional contendo o usuário, se encontrado
    */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
    Busca um usuário pelo nome de usuário
    @param username Nome de usuário
    @return Optional contendo o usuário, se encontrado
    */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
    Busca um usuário pelo e-mail
    @param email E-mail do usuário
    @return Optional contendo o usuário, se encontrado
    */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
    Registra um novo usuário
    @param user Usuário a ser registrado
    @return Usuário registrado
    @throws IllegalArgumentException se o nome de usuário ou e-mail já existirem
    */
    @Transactional
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Nome de usuário já existe");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("E-mail já existe");
        }

        // Criptografa a senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Adiciona papel de usuário comum se não houver papéis definidos
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName("ROLE_USER").ifPresent(roles::add);
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    /**
    Atualiza um usuário existente
    @param id ID do usuário
    @param userDetails Detalhes atualizados do usuário
    @return Usuário atualizado
    @throws IllegalArgumentException se o usuário não for encontrado
    */
    @Transactional
    public User update(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));

        // Atualiza apenas os campos permitidos
        user.setFullName(userDetails.getFullName());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setAddress(userDetails.getAddress());

        // Se a senha foi fornecida, atualiza-a
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
    Remove um usuário*
    @param id ID do usuário
    @throws IllegalArgumentException se o usuário não for encontrado
    */
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
    Adiciona um papel a um usuário
    @param userId ID do usuário
    @param roleName Nome do papel
    @return Usuário atualizado
    @throws IllegalArgumentException se o usuário ou papel não forem encontrados
    */
    @Transactional
    public User addRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Papel não encontrado com nome: " + roleName));

        user.getRoles().add(role);
        return userRepository.save(user);
    }
}
