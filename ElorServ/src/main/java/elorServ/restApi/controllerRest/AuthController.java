package elorServ.restApi.controllerRest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        
        boolean exito = passwordRecoveryService.recuperarPassword(username);
        
        if (exito) {
            return ResponseEntity.ok(Map.of("message", "Email enviado con éxito"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("error", "Usuario no encontrado o sin email registrado"));
        }
	}
}
