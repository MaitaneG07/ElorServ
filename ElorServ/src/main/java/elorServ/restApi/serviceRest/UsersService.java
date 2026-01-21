package elorServ.restApi.serviceRest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elorServ.modelo.entities.Users;
import elorServ.modelo.exception.ElorException;
import elorServ.restApi.repositoryRest.UsersRepository;


@Service
@Transactional
public class UsersService {
	
	 @Autowired
	    private UsersRepository usersRepository;

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

	public Users save(Users user) {
        return usersRepository.save(user);
	}

	public Users actualizar(Long id, Users usuario) {
		//quitar:
		return usuario;
//		return usuarioRepository.findById(id)
//	            .map(usuario -> {
//	                // Actualizar campos
//	                usuario.setNombre(usuarioActualizado.getNombre().trim());
//	                
//	                // Solo actualizar email si cambió y no existe
//	                if (!usuario.getEmail().equals(usuarioActualizado.getEmail())) {
//	                    if (usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
//	                        throw new IllegalArgumentException("El email ya está en uso");
//	                    }
//	                    usuario.setEmail(usuarioActualizado.getEmail().toLowerCase().trim());
//	                }
//	                
//	                // Actualizar password si se proporcionó
//	                if (usuarioActualizado.getPassword() != null && 
//	                    !usuarioActualizado.getPassword().isEmpty()) {
//	                    usuario.setPassword(usuarioActualizado.getPassword());
//	                }
//	                
//	                if (usuarioActualizado.getActivo() != null) {
//	                    usuario.setActivo(usuarioActualizado.getActivo());
//	                }
//	                
//	                return usuarioRepository.save(usuario);
//	            })
//	            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
//	   
	}

	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

	public Optional<Users> autenticar(String usuario, String password) {
		 // TODO: En producción, comparar con BCrypt
        return usersRepository.findByUserAndPassword(
            usuario.toLowerCase().trim(), 
            password
        );
	}

	public boolean existeEmail(String email) {
		return usersRepository.existsByEmail(email.toLowerCase().trim());
	}

	
}