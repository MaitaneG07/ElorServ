package elorServ.restApi.repositoryRest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    // Spring Data JPA genera automáticamente los métodos básicos
	Optional<Users> findFirstByNombreIgnoreCase(String nombre);
}