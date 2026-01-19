package elorServ.restApi.controllerRest;

import org.hibernate.mapping.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/api")
//public class ControladorRest {
//
//	@GetMapping("/users")
//	public List<Users> obtenerUsuarios() {
//		// Lógica para obtener usuarios
//		return listaUsuarios;
//	}
//
//	@GetMapping("/usuarios/{id}")
//	public User obtenerUsuario(@PathVariable Long id) {
//		return usuarioService.buscarPorId(id);
//	}
//
//	@PostMapping("/usuarios")User return usuarioService.guardar(usuario);
//
//	}
//
//	@PutMapping("/usuarios/{id}")
//    public User actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
//        return usuarioService.actualizar(id, usuario);
//    }
//
//	@DeleteMapping("/usuarios/{id}")
//    public void eliminarUsuario(@PathVariable Long id) {
//        usuarioService.eliminar(id);
//    }
//}}
