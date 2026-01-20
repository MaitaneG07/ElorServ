package elorServ.restApi.serviceRest;

import java.util.List;
import java.util.Optional;


import elorServ.modelo.entities.Users;
import elorServ.modelo.exception.ElorException;

public interface InterfaceService {
    
    List<Users> obtenerTodos();
    
    Optional<Users> obtenerPorId(Long id);
    
    Users crear(Users user) throws ElorException;
    
    Users actualizar(Long id, Users usuario);
    
    void eliminar(Long id);
    
    Optional<Users> autenticar(String usuario, String password);
    
    List<Users> buscarPorNombre(String nombre);
    
    boolean existeEmail(String email);
    
  
}