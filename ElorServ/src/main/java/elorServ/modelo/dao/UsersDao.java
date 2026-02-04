package elorServ.modelo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import elorServ.modelo.entities.Users;
import elorServ.modelo.util.HibernateUtil;

public class UsersDao extends GenericDao<Users>{

    public UsersDao() {
        super(Users.class);
    }
    

    /**
     * Obtiene alumnos de un profesor con filtros opcionales de ciclo y curso
     */
    public List<Users> getAlumnosByProfesorAndFilters(Integer profesorId, Integer cicloId, Integer curso) {
        List<Users> alumnos = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            System.out.println("[DAO] Buscando alumnos - Profesor: " + profesorId + 
                              ", Ciclo: " + cicloId + ", Curso: " + curso);
            
            StringBuilder hql = new StringBuilder(
                "SELECT DISTINCT u FROM Users u " +
                "JOIN Matriculaciones m ON m.users.id = u.id " +
                "JOIN Modulos mo ON mo.ciclos.id = m.ciclos.id AND mo.curso = m.curso " +
                "JOIN Horarios h ON h.modulos.id = mo.id " +
                "WHERE h.profesor.id = :profesorId " +  // ← CORREGIDO: h.profesor
                "AND u.tipos.id = 4"
            );
            
            if (cicloId != null) {
                hql.append(" AND m.ciclos.id = :cicloId");
            }
            
            if (curso != null) {
                hql.append(" AND m.curso = :curso");
            }
            
            hql.append(" ORDER BY u.apellidos, u.nombre");
            
            System.out.println("[DAO] HQL: " + hql.toString());
            
            Query<Users> query = session.createQuery(hql.toString(), Users.class);
            query.setParameter("profesorId", profesorId);
            
            if (cicloId != null) {
                query.setParameter("cicloId", cicloId);
            }
            
            if (curso != null) {
                query.setParameter("curso", curso);
            }
            
            alumnos = query.list();
            tx.commit();
            
            System.out.println("[DAO] Alumnos encontrados: " + (alumnos != null ? alumnos.size() : 0));
            
        } catch (Exception e) {
            System.out.println("[DAO ERROR] " + e.getMessage());
            e.printStackTrace();
        }
        
        return alumnos;
    }
    
    /**
     * Obtiene profesores con filtros opcionales de ciclo y curso
     */
    public List<Users> getProfesoresByFilters(Integer cicloId, Integer curso) {
        List<Users> profesores = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            System.out.println("[DAO] Buscando profesores - Ciclo: " + cicloId + ", Curso: " + curso);

            StringBuilder hql = new StringBuilder();
            hql.append("SELECT DISTINCT p ");
            hql.append("FROM Users p ");
            hql.append("WHERE p.tipos.id = 3 ");
            
            if (cicloId != null || curso != null) {
                hql.append("AND EXISTS (");
                hql.append("    SELECT 1 ");
                hql.append("    FROM Horarios h ");
                hql.append("    JOIN h.modulos mo ");
                hql.append("    WHERE h.profesor.id = p.id ");  // ← CORREGIDO: h.profesor
                
                if (cicloId != null) {
                    hql.append("    AND mo.ciclos.id = :cicloId ");
                }
                
                if (curso != null) {
                    hql.append("    AND mo.curso = :curso ");
                }
                
                hql.append(") ");
            }
            
            hql.append("ORDER BY p.apellidos, p.nombre");

            System.out.println("[DAO] HQL: " + hql.toString());

            Query<Users> query = session.createQuery(hql.toString(), Users.class);

            if (cicloId != null) {
                query.setParameter("cicloId", cicloId);
            }

            if (curso != null) {
                query.setParameter("curso", curso);
            }

            profesores = query.list();
            tx.commit();
            
            System.out.println("[DAO] Profesores encontrados: " + profesores.size());
            
        } catch (Exception e) {
            System.err.println("[DAO ERROR] " + e.getMessage());
            e.printStackTrace();
        }

        return profesores;
    }
}