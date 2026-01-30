package elorServ.restApi.repositoryRest;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    
    // Spring Data JPA genera automáticamente la implementación
    
    // Buscar por email
    Optional<Users> findByEmail(String email);
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
    
    // Buscar por nombre (ignora mayúsculas/minúsculas)
    List<Users> findByNombreContainingIgnoreCase(String nombre);
    
    
    // Buscar por email y password (para login)
    Optional<Users> findByUsernameAndPassword(String username, String password);

    
    Optional<Users> findByUsername(String username);
    
 
}