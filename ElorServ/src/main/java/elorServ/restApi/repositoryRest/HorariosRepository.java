package elorServ.restApi.repositoryRest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Horarios;

@Repository
public interface HorariosRepository extends JpaRepository<Horarios, Long>{

}
