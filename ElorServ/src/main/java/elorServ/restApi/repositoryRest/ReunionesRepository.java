package elorServ.restApi.repositoryRest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Reuniones;

@Repository
public interface ReunionesRepository extends JpaRepository<Reuniones, Long> {

}
