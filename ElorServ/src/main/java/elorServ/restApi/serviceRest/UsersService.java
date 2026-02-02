package elorServ.restApi.serviceRest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import elorServ.modelo.entities.Users;
import elorServ.restApi.dto.AlumnoTablaDto;
import elorServ.restApi.dto.PerfilAlumnoDto;
import elorServ.restApi.repositoryRest.UsersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Service
@Transactional
public class UsersService implements InterfaceService<Users>{
	
	 @Autowired
	    private UsersRepository usersRepository;

	    @Autowired
	    private SessionFactory session; 

	    @Autowired
	    private EmailService emailService; 
	    
	 @Override
	public List<Users> findAll() {
		return usersRepository.findAll();
	}

	public Optional<Users> findById(Long id) {
		return usersRepository.findById(id);
	}
	
	public List<Users> buscarPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users save(Users username) {
        return usersRepository.save(username);
	}

	@Override
	public Users actualizar(Long id, Users username) {
		return username;
	}

	@Override
	public void deleteById(Long id) {
		 usersRepository.deleteById(id);

	}

	public Optional<Users> autenticar(String username, String password) {
		 // TODO: En producción, comparar con BCrypt
        return usersRepository.findByUsernameAndPassword(
        		username.toLowerCase().trim(), 
            password
        );
	}

	public boolean existeEmail(String email) {
		return usersRepository.existsByEmail(email.toLowerCase().trim());
	}
	
	@PersistenceContext
	private EntityManager em;

	public Optional<PerfilAlumnoDto> obtenerPerfilAlumno(Integer userId) {
	    String sql = """
	        SELECT 
	          u.id as userId,
	          u.nombre as nombre,
	          u.apellidos as apellidos,
	          u.email as email,
	          c.id as cicloId,
	          c.nombre as cicloNombre,
	          m.curso as curso,
	          m.fecha as fechaMatricula
	        FROM users u
	        JOIN matriculaciones m ON m.alum_id = u.id
	        JOIN ciclos c ON c.id = m.ciclo_id
	        WHERE u.id = :userId
	        ORDER BY m.fecha DESC
	        LIMIT 1
	        """;

	    Query q = em.createNativeQuery(sql);
	    q.setParameter("userId", userId);

	    @SuppressWarnings("unchecked")
	    List<Object[]> rows = q.getResultList();

	    if (rows.isEmpty()) return Optional.empty();

	    Object[] row = rows.get(0);

	    Integer uId = ((Number) row[0]).intValue();
	    String nombre = (String) row[1];
	    String apellidos = (String) row[2];
	    String email = (String) row[3];
	    Integer cicloId = ((Number) row[4]).intValue();
	    String cicloNombre = (String) row[5];
	    Integer curso = ((Number) row[6]).intValue();
	    Date fechaSql = (Date) row[7];

	    PerfilAlumnoDto dto = new PerfilAlumnoDto(
	        uId,
	        nombre,
	        apellidos,
	        email,
	        cicloId,
	        cicloNombre,
	        curso,
	        fechaSql != null ? fechaSql.toLocalDate() : null
	    );

	    return Optional.of(dto);
	}
	
	public List<AlumnoTablaDto> obtenerAlumnosTabla() {

	    String sql = """
	        SELECT 
	            u.nombre,
	            u.apellidos,
	            c.nombre AS ciclo,
	            m.curso
	        FROM users u
	        JOIN matriculaciones m ON m.alum_id = u.id
	        JOIN ciclos c ON c.id = m.ciclo_id
	        WHERE u.tipo_id = 4
	        ORDER BY c.nombre, m.curso, u.apellidos
	    """;

	    Query q = em.createNativeQuery(sql);
	    @SuppressWarnings("unchecked")
	    List<Object[]> rows = q.getResultList();

	    return rows.stream()
	        .map(r -> new AlumnoTablaDto(
	            (String) r[0],
	            (String) r[1],
	            (String) r[2],
	            ((Number) r[3]).intValue()
	        ))
	        .toList();
	}

	
	public Users subirFotoPerfil(Long userId, MultipartFile file) throws Exception {
	    Users u = usersRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

	    if (file == null || file.isEmpty()) {
	        throw new IllegalArgumentException("Archivo vacío");
	    }

	    Path uploadDir = Paths.get("uploads", "users");
	    Files.createDirectories(uploadDir);

	    String original = file.getOriginalFilename();
	    String ext = "";
	    if (original != null && original.contains(".")) {
	        ext = original.substring(original.lastIndexOf("."));
	    }

	    String filename = "user_" + userId + "_" + UUID.randomUUID() + ext;
	    Path target = uploadDir.resolve(filename);

	    Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

	    String urlPublica = "/uploads/users/" + filename;

	    u.setArgazkiaUrl(urlPublica);
	    return usersRepository.save(u);
	}
	
	
	
}