package elorServ.restApi.serviceRest;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import elorServ.modelo.entities.Users;
import elorServ.restApi.repositoryRest.UsersRepository;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsersRepository usersRepository;

    // Constantes para el mensaje fijo
    private static final String ASUNTO_FIJO = "Notificación Automática del Sistema ElorServ";
    
    public void enviarCorreoFijo(String username) {
        // 1. Buscamos al usuario en la base de datos
        Optional<Users> userOpt = usersRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            Users usuario = userOpt.get();
            String destinatario = usuario.getEmail();

            // Validamos que tenga email
            if (destinatario != null && !destinatario.isEmpty()) {
                
                // 2. Construimos el cuerpo del mensaje usando los datos de tu entidad
                // Usamos StringBuilder para que sea más eficiente y limpio
                StringBuilder cuerpoBuilder = new StringBuilder();
                cuerpoBuilder.append("Hola ").append(usuario.getNombre()).append(",\n\n");
                cuerpoBuilder.append("Hemos recibido una solicitud de contacto desde la aplicación cliente.\n");
                cuerpoBuilder.append("Tus datos registrados son:\n");
                cuerpoBuilder.append("- DNI: ").append(usuario.getDni()).append("\n");
                cuerpoBuilder.append("- Teléfono: ").append(usuario.getTelefono1()).append("\n\n");
                cuerpoBuilder.append("Atentamente,\nEl equipo de ElorServ.");

                String cuerpoFinal = cuerpoBuilder.toString();

                // 3. Enviamos el correo
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("elorrieta96@gmail.com"); // Debe coincidir con application.properties
                message.setTo(destinatario);
                message.setSubject(ASUNTO_FIJO);
                message.setText(cuerpoFinal);

                mailSender.send(message);
                System.out.println("Correo fijo enviado a: " + destinatario);
                
            } else {
                System.out.println("El usuario " + usuario.getUsername() + " no tiene email.");
            }
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + username);
        }
    }
}
