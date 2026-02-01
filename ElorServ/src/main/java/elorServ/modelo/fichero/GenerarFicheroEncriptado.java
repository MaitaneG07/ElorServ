package elorServ.modelo.fichero;

import java.nio.file.Files;
import java.nio.file.Paths;

import elorServ.modelo.util.CryptoUtilsAES;

public class GenerarFicheroEncriptado {
	
	
	//Método para generar el fichero con la informacion encriptada
	public static void main(String[] args) {
        try {
            // Tus datos reales
            String email = "elorrieta96@gmail.com";
            String password = "tzbe aazi hrey uvwb";
            
            // Unimos los datos con un separador (ej: pipe |)
            String rawData = email + "|" + password;
            
            // Encriptamos
            String encryptedData = CryptoUtilsAES.encrypt(rawData);
            
            // Guardamos en un fichero
            Files.write(Paths.get("mail_config.txt"), encryptedData.getBytes());
            
            System.out.println("Fichero config.enc generado exitosamente.");
            System.out.println("Contenido encriptado: " + encryptedData);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	

}
