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
                r.profesor_id,
                r.alumno_id
            FROM reuniones r
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
            ((Number) r[4]).intValue(),
            ((Number) r[5]).intValue()
        )).toList();
    }

    public void cambiarEstado(Integer reunionId, Integer profesorIdLogeado, String nuevoEstado) {
        String sqlCheck = "SELECT COUNT(*) FROM reuniones WHERE id_reunion = :rid AND profesor_id = :pid";
        Query qCheck = em.createNativeQuery(sqlCheck);
        qCheck.setParameter("rid", reunionId);
        qCheck.setParameter("pid", profesorIdLogeado);

        Number count = (Number) qCheck.getSingleResult();
        if (count.intValue() == 0) {
            throw new IllegalArgumentException("No tienes permiso para modificar esta reunión");
        }

        String sqlUpd = "UPDATE reuniones SET estado = :estado, updated_at = NOW() WHERE id_reunion = :rid";
        Query qUpd = em.createNativeQuery(sqlUpd);
        qUpd.setParameter("estado", nuevoEstado);
        qUpd.setParameter("rid", reunionId);
        qUpd.executeUpdate();
    }
}
