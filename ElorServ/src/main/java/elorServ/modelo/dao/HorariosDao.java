package elorServ.modelo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import elorServ.modelo.entities.Horarios;
import elorServ.modelo.util.HibernateUtil;

public class HorariosDao extends GenericDao<Horarios> {

    public HorariosDao() {
        super(Horarios.class);
    }

    /**
     * Obtiene los horarios de un profesor específico
     * @param profesorId ID del profesor
     * @return Lista de horarios del profesor
     */
    public List<Horarios> selectHorarioByProfesorId(int profesorId) {
        List<Horarios> horarios = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("[DAO] INICIO - Buscando horarios para profesor: " + profesorId);

            Transaction tx = session.beginTransaction();

            String hql = "FROM Horarios h WHERE h.profesor.id = :profesorId ORDER BY h.dia, h.hora";
            System.out.println("[DAO] HQL: " + hql);

            Query<Horarios> query = session.createQuery(hql, Horarios.class);
            query.setParameter("profesorId", profesorId);

            horarios = query.list();

            System.out.println("[DAO] Horarios obtenidos de BD: " + (horarios != null ? horarios.size() : "null"));

            if (horarios != null && !horarios.isEmpty()) {
                System.out.println("[DAO] Primer horario - Día: " + horarios.get(0).getDia() + ", Hora: " + horarios.get(0).getHora());
            }

            tx.commit();

        } catch (Exception e) {
            System.out.println("[DAO ERROR] " + e.getMessage());
            e.printStackTrace();
        }

        return horarios;
    }

    /**
     * Obtiene los horarios por profesor y día de la semana
     */
    public List<Horarios> selectHorarioByProfesorAndDia(int profesorId, String diaSemana) {
        List<Horarios> horarios = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            String hql = "FROM Horarios h WHERE h.profesor.id = :profesorId AND h.dia = :dia ORDER BY h.hora";
            Query<Horarios> query = session.createQuery(hql, Horarios.class);
            query.setParameter("profesorId", profesorId);
            query.setParameter("dia", diaSemana);

            horarios = query.list();
            tx.commit();

        } catch (Exception e) {
            System.out.println("Error en selectHorarioByProfesorAndDia(): " + e.getMessage());
        }

        return horarios;
    }
}