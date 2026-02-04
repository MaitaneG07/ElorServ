package elorServ.restApi.serviceRest;


import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    
	
	 /**
     * Envia email de recuperacion de password a la app de movil
     */
    public void enviarNuevaPassword(String destinatario, String nuevaPassword, String username) {
        String remitente = "elorrieta96@gmail.com";
        String passwordEmail = "dtbn rbdv uogy aeix";
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, passwordEmail);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Recuperación de contraseña");
            
            String contenido = "Hola " + username + ",\n\n" +
                             "Has solicitado recuperar tu contraseña.\n\n" +
                             "Tu nueva contraseña es: " + nuevaPassword + "\n\n" +
                             "Por favor, cámbiala después de iniciar sesión por seguridad.\n\n" +
                             "Saludos.";
            
            message.setText(contenido);
            
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar email", e);
        }
    }
}
