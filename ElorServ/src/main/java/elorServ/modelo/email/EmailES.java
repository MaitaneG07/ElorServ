package elorServ.modelo.email;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import elorServ.modelo.exception.ElorException;
import elorServ.modelo.util.CryptoUtilsAES;

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
		String decrypted = CryptoUtilsAES.decrypt(content);

		// 3. Separar email y password (usamos el separador | que definimos en el Paso
		// 2)
		String[] parts = decrypted.split("\\|");

		if (parts.length == 2) {
			this.username = parts[0];
			this.password = parts[1];
		} else {
			throw new Exception("El formato del fichero de configuración es incorrecto.");
		}
	}

	
	
	public void enviarCorreoLogin(String destinatario, String nombreUsuario)  throws ElorException, AddressException, MessagingException {

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

		// Fragmente agregado por problemas de compatibilidad de la versión de java.
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
	}
	
	
	

	public void enviarConfirmacionReunion(String destinatario, String titulo, String aula, LocalDateTime fechaHora, String estado) throws ElorException, MessagingException{

		// Si no hay credenciales cargadas, salimos (asumiendo que el constructor ya las
		// cargó)
		if (username == null || password == null)
			return;

		// Configuración SMTP (Igual que antes)
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

			// Fix de compatibilidad (JavaMail)
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));

			// Asunto del correo
			message.setSubject("Nueva Reunión Creada: " + titulo);

			// Formatear la fecha para que se vea bien (ej: 30-01-2024 15:30)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			String fechaFormateada = fechaHora.format(formatter);

			// Cuerpo del mensaje
			String contenido = "Se ha programado una nueva reunión.\n\n" + "Título: " + titulo + "\n" + "Aula: " + aula
					+ "\n" + "Fecha y Hora: " + fechaFormateada + "\n" + "Estado " + estado + "\n\n" + "Acceda a su perfil para aceptar";

			message.setText(contenido);

			Transport.send(message);
			System.out.println("Correo de reunión enviado a: " + destinatario);

	}
	
	
	
	public void enviarActualizacionReunion(String destinatario, String estado) {

		// Si no hay credenciales cargadas, salimos (asumiendo que el constructor ya las
		// cargó)
		if (username == null || password == null)
			return;

		// Configuración SMTP (Igual que antes)
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Fix de compatibilidad (JavaMail)
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));

			// Asunto del correo
			message.setSubject("Actualización de reunión");


			// Cuerpo del mensaje
			String contenido = "Se ha actualizado la reunión a " + estado;

			message.setText(contenido);

			Transport.send(message);
			System.out.println("Correo de reunión enviado a: " + destinatario);

		} catch (MessagingException e) {
			System.err.println("Error enviando correo de reunión: " + e.getMessage());
			e.printStackTrace();
		}
	}
}