package elorServ.restApi.repositoryRest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Tipos;

@Repository
public interface TiposRepository extends JpaRepository<Tipos, Long> {

}
