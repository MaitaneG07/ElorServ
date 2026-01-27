package elorServ.restApi.repositoryRest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Ciclos;

@Repository
public interface CiclosRepository extends JpaRepository<Ciclos, Long> {

}
