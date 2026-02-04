package elorServ.restApi.serviceRest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elorServ.modelo.entities.Reuniones;
import elorServ.restApi.dto.ReunionListaDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
@Transactional
public class ReunionesService implements InterfaceService<Reuniones> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Reuniones save(Reuniones t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Reuniones> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Reuniones actualizar(Long id, Reuniones t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

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

        return rows.stream().map(r -> {

            LocalDateTime fecha = null;
            Object fechaObj = r[2];

            if (fechaObj instanceof Timestamp ts) {
                fecha = ts.toLocalDateTime();
            } else if (fechaObj instanceof LocalDateTime ldt) {
                fecha = ldt;
            }

            return new ReunionListaDto(
                ((Number) r[0]).intValue(),
                (String) r[1],
                fecha,
                (String) r[3],
                ((Number) r[4]).intValue(),
                ((Number) r[5]).intValue()
            );
        }).collect(Collectors.toList());
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
    
    public void crearReunion(
            Integer profesorId,
            Integer alumnoId,
            String titulo,
            String asunto,
            String aula,
            LocalDateTime fecha
    ) {
        String sql = """
            INSERT INTO reuniones
            (profesor_id, alumno_id, titulo, asunto, aula, fecha, estado)
            VALUES
            (:prof, :alum, :titulo, :asunto, :aula, :fecha, 'pendiente')
        """;

        Query q = em.createNativeQuery(sql);
        q.setParameter("prof", profesorId);
        q.setParameter("alum", alumnoId);
        q.setParameter("titulo", titulo);
        q.setParameter("asunto", asunto);
        q.setParameter("aula", aula);
        q.setParameter("fecha", fecha);

        q.executeUpdate();
    }
}