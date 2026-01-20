package elorServ.restApi.serviceRest;

import java.util.List;
import java.util.Optional;


import elorServ.modelo.entities.Users;

public interface InterfaceService {
    
    List<Users> obtenerTodos();
    
    Optional<Users> obtenerPorId(Long id);
    
    Users crear(Users user);
    
    Users actualizar(Long id, Users usuario);
    
    void eliminar(Long id);
    
    Optional<Users> autenticar(String usuario, String password);
    
    List<Users> buscarPorNombre(String nombre);
    
    List<Users> obtenerActivos();
    
    boolean existeEmail(String email);
    
    long contarUsuariosActivos();
}