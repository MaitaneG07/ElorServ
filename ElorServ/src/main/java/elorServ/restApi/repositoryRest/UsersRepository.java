package elorServ.restApi.repositoryRest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    // Spring Data JPA genera automáticamente los métodos básicos
}