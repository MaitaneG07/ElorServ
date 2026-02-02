package elorServ.restApi.serviceRest;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elorServ.restApi.dto.ReunionListaDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
@Transactional
public class ReunionesService {
    @PersistenceContext
    private EntityManager em;
	public List<ReunionListaDto> obtenerReunionesUsuario(Integer userId) {

	    String sql = """
	        SELECT
	            r.id_reunion,
	            r.titulo,
	            r.fecha,
	            r.estado,
	            CONCAT(p.nombre, ' ', p.apellidos) AS profesorNombre,
	            CONCAT(a.nombre, ' ', a.apellidos) AS alumnoNombre
	        FROM reuniones r
	        JOIN users p ON p.id = r.profesor_id
	        JOIN users a ON a.id = r.alumno_id
	        WHERE r.profesor_id = :userId OR r.alumno_id = :userId
	        ORDER BY r.fecha
	    """;

	    Query q = em.createNativeQuery(sql);
	    q.setParameter("userId", userId);

	    @SuppressWarnings("unchecked")
	    List<Object[]> rows = q.getResultList();

	    return rows.stream().map(r -> new ReunionListaDto(
	        ((Number) r[0]).intValue(),
	        (String) r[1],
	        (java.time.LocalDateTime) r[2],
	        (String) r[3],
	        (String) r[4],
	        (String) r[5]
	    )).toList();
	}
}
