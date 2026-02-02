package elorServ.restApi.controllerRest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import elorServ.restApi.serviceRest.PasswordRecoveryService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private PasswordRecoveryService passwordRecoveryService;
    
    @PostMapping("/recuperar-password")
    public ResponseEntity<?> recuperarPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Username requerido");
        }
        
        boolean exito = passwordRecoveryService.recuperarPassword(username);
        
        if (exito) {
            return ResponseEntity.ok("Se ha enviado una nueva contraseña al email registrado");
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }
}
