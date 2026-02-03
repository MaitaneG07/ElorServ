package elorServ.restApi.controllerRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import elorServ.restApi.serviceRest.EmailService;

//@RestController
//@RequestMapping("/api/mail")
//public class EmailController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @PostMapping("/notificar")
//    public ResponseEntity<String> enviarNotificacion(@RequestParam String username, String destinatario, String nuevaPassword) {
//        try {
//            emailService.enviarNuevaPassword(destinatario, username, nuevaPassword);
//            return ResponseEntity.ok("Correo enviado correctamente al usuario ID: " + username);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
//        }
//    }

@RestController
@RequestMapping("/api/mail")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/notificar")
    public ResponseEntity<String> enviarNotificacion(
            @RequestParam String username, 
            @RequestParam String destinatario, 
            @RequestParam String nuevaPassword) {
        
        // No más try-catch. Si algo falla, el GlobalExceptionHandler entra en acción.
        emailService.enviarNuevaPassword(destinatario, username, nuevaPassword);
        
        return ResponseEntity.ok("Correo enviado correctamente al usuario ID: " + username);
    }
}
