package elorServ.modelo.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import elorServ.modelo.entities.Reuniones;
import elorServ.modelo.util.HibernateUtil;

public class ReunionesDao extends GenericDao<Reuniones> {

	public ReunionesDao() {
		super(Reuniones.class);
	}

	/**
	 * Obtiene TODAS las reuniones de un profesor
	 * Método más simple y confiable
	 */
	public List<Reuniones> selectReunionesByProfesorId(int profesorId) {
		List<Reuniones> reuniones = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction tx = session.beginTransaction();

			System.out.println("[DAO] Buscando todas las reuniones del profesor ID: " + profesorId);

			String hql = "FROM Reuniones r " + "LEFT JOIN FETCH r.alumno " + "LEFT JOIN FETCH r.profesor "
					+ "WHERE r.profesor.id = :profesorId " + "ORDER BY r.fecha";

			Query<Reuniones> query = session.createQuery(hql, Reuniones.class);
			query.setParameter("profesorId", profesorId);

			reuniones = query.list();
			tx.commit();

			System.out.println("[DAO] Reuniones encontradas: " + (reuniones != null ? reuniones.size() : 0));

		} catch (Exception e) {
			System.out.println("[DAO ERROR] Error en selectReunionesByProfesorId: " + e.getMessage());
			e.printStackTrace();
		}

		return reuniones;
	}
	

}