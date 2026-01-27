package elorServ.restApi.repositoryRest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elorServ.modelo.entities.Modulos;

@Repository
public interface ModulosRepository extends JpaRepository<Modulos, Long> {

}
