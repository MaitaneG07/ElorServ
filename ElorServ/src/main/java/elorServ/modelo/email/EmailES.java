package elorServ.modelo.email;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailES {

    // Configuración para GMAIL
    private final String username = "elorrieta96@gmail.com";
    private final String password = "tzbe aazi hrey uvwb"; // OJO: Ver nota de seguridad abajo

    public void enviarCorreoLogin(String destinatario, String nombreUsuario) {
        
        // Propiedades del servidor SMTP
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        // Crear sesión con autenticación
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
        	
        	MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
            
            
            // Crear el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Nuevo inicio de sesión detectado");
            
            String contenido = "Hola " + nombreUsuario + ",\n\n"
                             + "Se ha detectado un nuevo inicio de sesión en tu cuenta.\n"
                             + "Si no has sido tú, contacta con soporte.";
            
            message.setText(contenido);

            // Enviar
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a: " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error enviando correo: " + e.getMessage());
        }
    }
}