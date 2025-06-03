package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 Repositório para operações de persistência da entidade User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
    Busca um usuário pelo nome de usuário
    @param username Nome de usuário
    @return Optional contendo o usuário, se encontrado
    */
    Optional<User> findByUsername(String username);
    
    /**
    Busca um usuário pelo e-mail 
    @param email E-mail do usuário
    @return Optional contendo o usuário, se encontrado
    */
    Optional<User> findByEmail(String email);
    
    /**
    Verifica se existe um usuário com o nome de usuário especificado
    @param username Nome de usuário
    @return true se existir, false caso contrário
    */
    boolean existsByUsername(String username);
    
    /**
    Verifica se existe um usuário com o e-mail especificado
    @param email E-mail do usuário
    @return true se existir, false caso contrário
    */
    boolean existsByEmail(String email);
}
