package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
Repositório para operações de persistência da entidade Role
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
    Busca um papel pelo nome
    @param name Nome do papel
    @return Optional contendo o papel, se encontrado
    */
    Optional<Role> findByName(String name);
    
    /**
    Verifica se existe um papel com o nome especificado
    @param name Nome do papel
    @return true se existir, false caso contrário
    */
    boolean existsByName(String name);
}
