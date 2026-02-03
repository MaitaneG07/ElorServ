package elorServ.restApi.serviceRest;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elorServ.modelo.entities.Users;
import elorServ.restApi.repositoryRest.UsersRepository;


@Service
public class PasswordRecoveryService {
    
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Busca el usuario, si existe, le cambia la contraseña, encripta la nueva, 
     * la guarda encriptada en la base de datos y manda un email con la 
     * contraseña nueva sin encriptar 
     */
    @Transactional
    public boolean recuperarPassword(String username) {
        // 1. Buscar usuario (El Optional se maneja con .isPresent() o .orElse())
        Optional<Users> usuarioOpt = usersRepository.findByUsername(username);
        
        if (!usuarioOpt.isPresent()) {
            return false;
        }

        // 2. Extraer el usuario del contenedor
        Users usuario = usuarioOpt.get();
        
        String emailUsuario = usuario.getEmail();
        if (emailUsuario == null || emailUsuario.isEmpty()) {
            return false; 
        }
        
        // 3. Generar y cifrar
        String nuevaPassword = generarPasswordAleatoria();
        String passwordCifrada = passwordEncoder.encode(nuevaPassword);
        
        // 4. Actualizar la entidad real
        usuario.setPassword(passwordCifrada);
        usersRepository.save(usuario);
        
        // 5. Enviar por email
        emailService.enviarNuevaPassword(emailUsuario, nuevaPassword, username);
        
        return true;
    }
    
    private String generarPasswordAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < 12; i++) {
            password.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        
        return password.toString();
    }

}
