package elorServ.modelo.dao;

import java.util.List;

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
     * Obtiene TODAS las reuniones de un profesor (sin filtrar por ciclo/curso)
     * Método más simple y confiable
     */
    public List<Reuniones> selectReunionesByProfesorId(int profesorId) {
        List<Reuniones> reuniones = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            System.out.println("[DAO] Buscando todas las reuniones del profesor ID: " + profesorId);
            
            String hql = "FROM Reuniones r " +
                        "LEFT JOIN FETCH r.alumno " +
                        "LEFT JOIN FETCH r.profesor " +
                        "WHERE r.profesor.id = :profesorId " +
                        "ORDER BY r.fecha";
            
            Query<Reuniones> query = session.createQuery(hql, Reuniones.class);
            query.setParameter("profesorId", profesorId);
            
            reuniones = query.list();
            tx.commit();
            
            System.out.println("[DAO] Reuniones encontradas: " + 
                              (reuniones != null ? reuniones.size() : 0));
            
        } catch (Exception e) {
            System.out.println("[DAO ERROR] Error en selectReunionesByProfesorId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reuniones;
    }
    
    /**
     * Actualiza el estado de una reunión
     * @param idReunion ID de la reunión a actualizar
     * @param nuevoEstado Nuevo estado de la reunión (pendiente, aceptada, denegada, conflicto)
     * @return true si se actualizó correctamente, false en caso contrario
     */
    @SuppressWarnings("deprecation")
	public boolean actualizarEstadoReunion(int idReunion, String nuevoEstado) {
        Transaction tx = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            
            System.out.println("[DAO] Actualizando reunión ID: " + idReunion + " al estado: " + nuevoEstado);
            
            Reuniones reunion = session.get(Reuniones.class, idReunion);
            
            if (reunion != null) {
                reunion.setEstado(nuevoEstado);
                
                session.update(reunion);
                
                tx.commit();
                
                System.out.println("[DAO] Reunión ID " + idReunion + " actualizada exitosamente a estado: " + nuevoEstado);
                return true;
            } else {
                System.out.println("[DAO ERROR] No se encontró la reunión con ID: " + idReunion);
                if (tx != null) {
                    tx.rollback();
                }
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("[DAO ERROR] Error al actualizar reunión: " + e.getMessage());
            e.printStackTrace();
            
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    System.out.println("[DAO ERROR] Error en rollback: " + ex.getMessage());
                }
            }
            return false;
        }
    }

}