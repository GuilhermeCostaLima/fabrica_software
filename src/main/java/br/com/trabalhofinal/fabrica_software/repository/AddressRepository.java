package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 Repositório para operações de persistência da entidade Address.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    /**
    Busca endereços pela cidade
    @param city Nome da cidade
    @return Lista de endereços encontrados
    */
    List<Address> findByCity(String city);
    
    /**
    Busca endereços pelo estado
    @param state Nome do estado
    @return Lista de endereços encontrados
    */
    List<Address> findByState(String state);
    
    /**
    Busca endereços pelo país
    @param country Nome do país
    @return Lista de endereços encontrados
    */
    List<Address> findByCountry(String country);
    
    /**
    Busca endereços pelo CEP 
    @param zipCode CEP
    @return Lista de endereços encontrados
    */
    List<Address> findByZipCode(String zipCode);
}
