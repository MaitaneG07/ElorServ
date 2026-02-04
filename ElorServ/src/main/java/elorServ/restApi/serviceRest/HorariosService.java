package elorServ.restApi.serviceRest;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elorServ.restApi.dto.HorarioProfesorDto;
import elorServ.restApi.dto.HorariosDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
@Transactional(readOnly = true)
public class HorariosService {

    @PersistenceContext
    private EntityManager em;

    public HorarioProfesorDto obtenerHorarioProfesor(Integer profesorId) {

        String sqlProfe = """
            SELECT u.id, CONCAT(u.nombre, ' ', u.apellidos) as nombreCompleto
            FROM users u
            WHERE u.id = :pid
            """;

        Query qProfe = em.createNativeQuery(sqlProfe);
        qProfe.setParameter("pid", profesorId);

        @SuppressWarnings("unchecked")
        List<Object[]> profRows = qProfe.getResultList();

        if (profRows.isEmpty()) {
            return new HorarioProfesorDto(profesorId, null, List.of());
        }

        Object[] pr = profRows.get(0);
        Integer pid = ((Number) pr[0]).intValue();
        String profeNombre = (String) pr[1];

        String sql = """
            SELECT
              h.dia,
              h.hora,
              h.aula,
              h.observaciones,
              m.id as moduloId,
              m.nombre as moduloNombre,
              m.curso as curso,
              c.nombre as cicloNombre
            FROM horarios h
            JOIN modulos m ON m.id = h.modulo_id
            JOIN ciclos c ON c.id = m.ciclo_id
            WHERE h.profe_id = :pid
            ORDER BY
              FIELD(h.dia,'LUNES','MARTES','MIERCOLES','JUEVES','VIERNES'),
              h.hora
            """;

        Query q = em.createNativeQuery(sql);
        q.setParameter("pid", profesorId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();

        List<HorariosDto> slots = new ArrayList<>();

        for (Object[] r : rows) {
            String dia = (String) r[0];
            Integer hora = ((Number) r[1]).intValue();
            String aula = (String) r[2];
            String observaciones = (String) r[3];

            Integer moduloId = ((Number) r[4]).intValue();
            String moduloNombre = (String) r[5];
            Integer curso = r[6] != null ? ((Number) r[6]).intValue() : null;
            String cicloNombre = (String) r[7];

            String tipo;
            Integer cursoOut = null;
            String cicloOut = null;

            if (moduloId == 1) {
                tipo = "TUTORIA";
            } else if (moduloId == 2) {
                tipo = "GUARDIA";
            } else {
                tipo = "CLASE";
                cursoOut = curso;
                cicloOut = cicloNombre;
            }

            slots.add(new HorariosDto(
                dia,
                hora,
                tipo,
                cursoOut,
                cicloOut,
                moduloNombre,
                aula,
                observaciones
            ));
        }

        return new HorarioProfesorDto(pid, profeNombre, slots);
    }
   

    public HorarioProfesorDto obtenerHorarioAlumno(Integer alumnoId) {

        String sqlMat = """
            SELECT m.ciclo_id, m.curso
            FROM matriculaciones m
            WHERE m.alum_id = :alumId
            ORDER BY m.fecha DESC
            LIMIT 1
            """;

        Query qMat = em.createNativeQuery(sqlMat);
        qMat.setParameter("alumId", alumnoId);

        @SuppressWarnings("unchecked")
        List<Object[]> matRows = qMat.getResultList();

        if (matRows.isEmpty()) {
            return new HorarioProfesorDto(alumnoId, null, List.of());
        }

        Object[] mr = matRows.get(0);
        Integer cicloId = ((Number) mr[0]).intValue();
        Integer curso = ((Number) mr[1]).intValue();

        String sqlHor = """
            SELECT
              h.dia,
              h.hora,
              h.aula,
              h.observaciones,
              m.curso,
              c.nombre AS cicloNombre,
              m.nombre AS moduloNombre
            FROM horarios h
            JOIN modulos m ON m.id = h.modulo_id
            JOIN ciclos  c ON c.id = m.ciclo_id
            WHERE m.ciclo_id = :cicloId
              AND m.curso    = :curso
              AND m.id NOT IN (1,2)
            ORDER BY
              FIELD(h.dia,'LUNES','MARTES','MIERCOLES','JUEVES','VIERNES'),
              h.hora
            """;

        Query q = em.createNativeQuery(sqlHor);
        q.setParameter("cicloId", cicloId);
        q.setParameter("curso", curso);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();

        List<HorariosDto> slots = new java.util.ArrayList<>();

        for (Object[] r : rows) {
            String dia = (String) r[0];
            Integer hora = ((Number) r[1]).intValue();
            String aula = (String) r[2];
            String observaciones = (String) r[3];
            Integer cursoOut = ((Number) r[4]).intValue();
            String cicloNombre = (String) r[5];
            String moduloNombre = (String) r[6];

            slots.add(new HorariosDto(
                dia,
                hora,
                "CLASE",
                cursoOut,
                cicloNombre,
                moduloNombre,
                aula,
                observaciones
            ));
        }

        return new HorarioProfesorDto(alumnoId, null, slots);
    }

}
