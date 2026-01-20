package elorServ.restApi.serviceRest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elorServ.modelo.entities.Users;
import elorServ.restApi.repositoryRest.UsersRepository;


@Service
@Transactional
public class UsersService implements InterfaceService {
	
	 @Autowired
	    private UsersRepository usersRepository;

	@Override
	public List<Users> obtenerTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Users> obtenerPorId(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Users crear(Users user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users actualizar(Long id, Users usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<Users> autenticar(String usuario, String password) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Users> buscarPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Users> obtenerActivos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existeEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long contarUsuariosActivos() {
		// TODO Auto-generated method stub
		return 0;
	}
}