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
public class UsersService implements InterfaceService {
	
	 @Autowired
	    private UsersRepository usersRepository;

	@Override
	public List<Users> obtenerTodos() {
		return usersRepository.findAll();
	}

	@Override
	public Optional<Users> obtenerPorId(Long id) {
		return usersRepository.findById(id);
	}

	@Override
	public Users crear(Users user) throws ElorException {
		if (usersRepository.existsByEmail(user.getEmail())) {
            throw new ElorException();
	}
		// Normalizar datos
		user.setEmail(user.getEmail().toLowerCase().trim());
		user.setNombre(user.getNombre().trim());
        
        // TODO: En producción, hashear la contraseña con BCrypt
        // String passwordHash = passwordEncoder.encode(usuario.getPassword());
        // usuario.setPassword(passwordHash);
        
        return usersRepository.save(user);
	}

	@Override
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

	@Override
	public void eliminar(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<Users> autenticar(String usuario, String password) {
		 // TODO: En producción, comparar con BCrypt
        return usersRepository.findByEmailAndPassword(
            usuario.toLowerCase().trim(), 
            password
        );
	}

	@Override
	public List<Users> buscarPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public boolean existeEmail(String email) {
		return usersRepository.existsByEmail(email.toLowerCase().trim());
	}

	
}