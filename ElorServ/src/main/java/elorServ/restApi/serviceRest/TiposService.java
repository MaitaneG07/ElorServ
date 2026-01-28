package elorServ.restApi.serviceRest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elorServ.modelo.entities.Tipos;
import elorServ.restApi.repositoryRest.TiposRepository;

@Service
@Transactional
public class TiposService {
	
	@Autowired
    private TiposRepository tiposRepos;
	

		public List<Tipos> findAll() {
			return tiposRepos.findAll();
		}

}
