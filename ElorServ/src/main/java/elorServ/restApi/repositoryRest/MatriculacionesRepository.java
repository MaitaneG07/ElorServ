package elorServ.restApi.repositoryRest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Matriculaciones;

@Repository
public interface MatriculacionesRepository extends JpaRepository<Matriculaciones, Long> {

}
