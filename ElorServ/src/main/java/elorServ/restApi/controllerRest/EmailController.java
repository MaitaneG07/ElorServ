package elorServ.restApi.controllerRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import elorServ.restApi.serviceRest.EmailService;

@RestController
@RequestMapping("/api/mail")
public class EmailController {

    @Autowired
    private EmailService emailService;

//    @PostMapping("/notificar")
//    public ResponseEntity<String> enviarNotificacion(@RequestParam String username) {
//        try {
//            // Ya no pasamos asunto ni cuerpo, solo el ID
//            emailService.enviarCorreoFijo(username);
//            return ResponseEntity.ok("Correo enviado correctamente al usuario ID: " + username);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
//        }
//    }
}
