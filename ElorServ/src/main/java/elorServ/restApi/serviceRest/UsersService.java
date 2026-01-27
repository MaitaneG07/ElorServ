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
public class UsersService implements InterfaceService<Users>{
	
	 @Autowired
	    private UsersRepository usersRepository;

	 @Override
	public List<Users> findAll() {
		return usersRepository.findAll();
	}

	public Optional<Users> findById(Long id) {
		return usersRepository.findById(id);
	}
	
	public List<Users> buscarPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users save(Users username) {
        return usersRepository.save(username);
	}

	@Override
	public Users actualizar(Long id, Users username) {
		return username;
	}

	@Override
	public void deleteById(Long id) {
		 usersRepository.deleteById(id);

	}

	public Optional<Users> autenticar(String username, String password) {
		 // TODO: En producción, comparar con BCrypt
        return usersRepository.findByUsernameAndPassword(
        		username.toLowerCase().trim(), 
            password
        );
	}

	public boolean existeEmail(String email) {
		return usersRepository.existsByEmail(email.toLowerCase().trim());
	}

	
}