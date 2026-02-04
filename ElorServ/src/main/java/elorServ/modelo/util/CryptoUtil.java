package elorServ.modelo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * Utilidad para encriptación RSA en el servidor
 * Genera claves pública/privada y permite desencriptar contraseñas
 */
public class CryptoUtil {

    private static final String PUBLIC_KEY_FILE = "server_public.key";
    private static final String PRIVATE_KEY_FILE = "server_private.key";
    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final int KEY_SIZE = 2048;

    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    /**
     * Inicializa las claves RSA. Si no existen, las genera.
     */
    public static void inicializar() {
        try {
            File publicKeyFile = new File(PUBLIC_KEY_FILE);
            File privateKeyFile = new File(PRIVATE_KEY_FILE);

            if (publicKeyFile.exists() && privateKeyFile.exists()) {
                System.out.println("[CRYPTO] Cargando claves RSA existentes...");
                cargarClaves();
            } else {
                System.out.println("[CRYPTO] Generando nuevas claves RSA...");
                generarYGuardarClaves();
            }

            System.out.println("[CRYPTO] Sistema de encriptación RSA inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[CRYPTO ERROR] Error al inicializar sistema de encriptación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Genera un par de claves RSA y las guarda en archivos
     */
    private static void generarYGuardarClaves() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(KEY_SIZE);
        KeyPair keyPair = generator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        try (FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_FILE)) {
            fos.write(x509EncodedKeySpec.getEncoded());
        }

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        try (FileOutputStream fos = new FileOutputStream(PRIVATE_KEY_FILE)) {
            fos.write(pkcs8EncodedKeySpec.getEncoded());
        }

        System.out.println("[CRYPTO] Claves RSA generadas y guardadas");
    }

    /**
     * Carga las claves desde los archivos
     */
    private static void cargarClaves() throws Exception {
    	
        File publicKeyFile = new File(PUBLIC_KEY_FILE);
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = keyFactory.generatePublic(publicKeySpec);

        File privateKeyFile = new File(PRIVATE_KEY_FILE);
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        privateKey = keyFactory.generatePrivate(privateKeySpec);
    }

    /**
     * Obtiene la clave pública en formato Base64 para enviar al cliente
     * @return Clave pública codificada en Base64
     */
    public static String getPublicKeyBase64() {
        if (publicKey == null) {
            inicializar();
        }
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * Desencripta un texto que fue encriptado con la clave pública
     * @param textoEncriptado Texto encriptado en Base64
     * @return Texto desencriptado
     */
    public static String desencriptar(String textoEncriptado) {
        try {
            if (privateKey == null) {
                inicializar();
            }

            byte[] encryptedBytes = Base64.getDecoder().decode(textoEncriptado);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            
            return new String(decryptedBytes);
        } catch (Exception e) {
            System.err.println("[CRYPTO ERROR] Error al desencriptar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método para probar el sistema de encriptación
     */
    public static void main(String[] args) {
        
        CryptoUtil.inicializar();
        
        System.out.println("\nClave pública (Base64): ");
        System.out.println(CryptoUtil.getPublicKeyBase64());
    }
}