package elorServ.modelo.email;

import java.nio.file.Files;
import java.nio.file.Paths;
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

import elorServ.modelo.entities.Reuniones;
import elorServ.modelo.util.CryptoUtils;


public class EmailES {

    // Configuración para GMAIL
    private String username;
    private String password;
    
 // Constructor: para cargar y desencriptar las credenciales
    public EmailES() {
        try {
            cargarCredenciales();
        } catch (Exception e) {
            System.err.println("Error crítico: No se pudieron cargar las credenciales de correo.");
            e.printStackTrace();
        }
    }
    
    private void cargarCredenciales() throws Exception {
        // 1. Leer el fichero encriptado
        String content = new String(Files.readAllBytes(Paths.get("config.enc")));
        
        // 2. Desencriptar
        String decrypted = CryptoUtils.decrypt(content);
        
        // 3. Separar email y password (usamos el separador | que definimos en el Paso 2)
        String[] parts = decrypted.split("\\|");
        
        if (parts.length == 2) {
            this.username = parts[0];
            this.password = parts[1];
        } else {
            throw new Exception("El formato del fichero de configuración es incorrecto.");
        }
    }

    public void enviarCorreoLogin(String destinatario, String nombreUsuario) {
        
    	if (this.username == null || this.password == null) {
            System.err.println("No se puede enviar el correo: Credenciales no cargadas.");
            return;
        }
    	
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
        	//Fragmente agregado por problemas de compatibilidad de la versión de java.
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
    
    
    
public void enviarCorreoReunion(String destinatario, String nombreUsuario) {
        
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
        	//Fragmente agregado por problemas de compatibilidad de la versión de java.
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
            message.setSubject("Reunion");
            
            String contenido = "Hola " + nombreUsuario + ",\n\n"
                             + "Se ha detectado un nueva reunion.\n";
                     
            
            message.setText(contenido);

            // Enviar
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a: " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error enviando correo: " + e.getMessage());
        }
    }

//Método nuevo dentro de EmailService.java
//Añade esto en EmailES.java
//Añade esto en EmailES.java
public void enviarCorreoReunion(String destinatario, String nombreAlumno, Reuniones reunion) {
 // Copia aquí la misma configuración de Properties que tienes en enviarCorreoLogin
 Properties prop = new Properties();
 prop.put("mail.smtp.host", "smtp.gmail.com");
 prop.put("mail.smtp.port", "587");
 prop.put("mail.smtp.auth", "true");
 prop.put("mail.smtp.starttls.enable", "true");
 prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

 Session session = Session.getInstance(prop, new Authenticator() {
     @Override
     protected PasswordAuthentication getPasswordAuthentication() {
         return new PasswordAuthentication("tucorreo@gmail.com", "tu_password_app");
     }
 });

 try {
     // FIX para el error de ClassLoader
     Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

     Message message = new MimeMessage(session);
     message.setFrom(new InternetAddress("tucorreo@gmail.com"));
     message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
     message.setSubject("Nueva Reunión: " + reunion.getTitulo());

     // Cuerpo del mensaje
     String fechaStr = (reunion.getFecha() != null) ? reunion.getFecha().toString().replace("T", " ") : "Fecha por definir";
     
     String contenido = "Hola " + nombreAlumno + ",\n\n"
                      + "Se ha programado una nueva reunión contigo.\n"
                      + "------------------------------------------------\n"
                      + "📅 Fecha: " + fechaStr + "\n"
                      + "📍 Aula: " + reunion.getAula() + "\n"
                      + "📝 Asunto: " + reunion.getAsunto() + "\n"
                      + "------------------------------------------------\n\n"
                      + "Por favor, confirma tu asistencia.";

     message.setText(contenido);
     Transport.send(message);
     System.out.println("[EMAIL] Invitación enviada a " + destinatario);

 } catch (MessagingException e) {
     System.err.println("[EMAIL ERROR] " + e.getMessage());
     e.printStackTrace();
 }
}
}