package elorServ.restApi.controllerRest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import elorServ.modelo.entities.Users;
import elorServ.modelo.exception.ElorException;
import elorServ.restApi.serviceRest.UsersService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/users")
public class UsersController {
    
    @Autowired
    private UsersService usersService;
    
    /**
     * GET /api/users
     * Obtener todos los users
     */
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = usersService.findAll();
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/users/{id}
     * Obtener un user por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Users> user = usersService.findById(id);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Users no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    
    
    /**
     * POST /api/users
     * Crear un nuevo user
     * @throws ElorException 
     */
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Users username) throws ElorException {
        try {
            Users usuarioCreado = usersService.save(username);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * PUT /api/users/{id}
     * Actualizar un user existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Users username) {
        try {
            Users usuarioActualizado = usersService.actualizar(id, username);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * DELETE /api/users/{id}
     * Eliminar un user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usersService.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Users eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * POST /api/users/login
     * Autenticar user
     */
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
//        String username = credenciales.get("username");
//        String password = credenciales.get("password");
//        
//        Optional<Users> user = usersService.autenticar(username, password);
//        
//        if (user.isPresent()) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("mensaje", "Login exitoso");
//            response.put("user", user.get());
//            return ResponseEntity.ok(response);
//        } else {
//            Map<String, String> error = new HashMap<>();
//            error.put("error", "Credenciales inválidas");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//        }
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        // 1. Intentamos obtener "email" o "username" para ser flexibles
        String usuario = credenciales.get("email");
        if (usuario == null) {
            usuario = credenciales.get("username");
        }

        String password = credenciales.get("password");
        
        // 2. VALIDACIÓN DE SEGURIDAD (Esto evita el NullPointerException)
        if (usuario == null || password == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Faltan datos: se requiere 'email' (o 'username') y 'password'");
            // Imprimimos para ver qué claves llegaron realmente
            System.out.println("Claves recibidas: " + credenciales.keySet()); 
            return ResponseEntity.badRequest().body(error);
        }
        
        // 3. Llamamos al servicio con el dato seguro
        Optional<Users> user = usersService.autenticar(usuario, password);
        
        if (user.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("user", user.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    /**
     * GET /api/users/buscar?nombre=texto
     * Buscar users por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<Users> getByName(@RequestParam String nombre) {
    	Users ret = null;
        List<Users> users = usersService.buscarPorNombre(nombre);
        
        for (Users user : users) {
            if (user.getNombre() != null && user.getNombre().equals(nombre)) {
                ret = user;
                break;
            }
        }
        
        return ret != null
                ? ResponseEntity.ok(ret)
                : ResponseEntity.notFound().build();
    }
    
    
  
    
    /**
     * GET /api/users/existe-email?email=test@example.com
     * Verificar si un email existe
     */
    @GetMapping("/existe-email")
    public ResponseEntity<?> existeEmail(@RequestParam String email) {
        boolean existe = usersService.existeEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", existe);
        return ResponseEntity.ok(response);
    }
}

//@RestController
//@RequestMapping("/api")
//public class UsersController {
//
//	 @Autowired
//	private UsersService usersService;
//    private final UsersRepository usersRepository;
//
//    public UsersController(UsersRepository usersRepository) {
//        this.usersRepository = usersRepository;
//    }
//    
//    /**
//     * GET /api/users
//     * Obtener todos los users
//     */
//    @GetMapping
//    public ResponseEntity<List<Users>> obtenerTodos() {
//        List<Users> users = usersService.obtenerTodos();
//        return ResponseEntity.ok(users);
//    }
//    
//
//    @GetMapping("/users")
//    public List<Users> getAllUsers() {
//        return usersRepository.findAll();
//    }
//    
//    @GetMapping("/user/find/{name}")
//    public ResponseEntity<Users> getByName(@PathVariable String name) {
//
//        Users ret = null;
//
//        List<Users> users = usersRepository.findAll();
//
//        for (Users user : users) {
//            if (user.getNombre() != null && user.getNombre().equals(name)) {
//                ret = user;
//                break;
//            }
//        }
//
//        return ret != null
//                ? ResponseEntity.ok(ret)
//                : ResponseEntity.notFound().build();
//    }
//
//    
//    @PostMapping("/user/new")
//    public void addUser(@RequestBody Users user) {
//        usersRepository.save(user);
//    }
//}

