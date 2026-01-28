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
     * @param profesorId ID del profesor
     * @param cicloId ID del ciclo (null para todos)
     * @param curso Número de curso (null para todos)
     * @return Lista de alumnos que cumplen los criterios
     */
    public List<Users> getAlumnosByProfesorAndFilters(
            Integer profesorId,
            Integer cicloId,
            Integer curso) {

        List<Users> alumnos;

        System.out.println("[DAO] Buscando alumnos - Profesor: " + profesorId + ", Ciclo: " + cicloId + ", Curso: " + curso);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            StringBuilder hql = new StringBuilder();
            hql.append("SELECT DISTINCT u ");
            hql.append("FROM Users u ");
            hql.append("JOIN Matriculaciones m ON m.users = u ");  // Cambiado: m.users en lugar de m.alumId
            hql.append("JOIN Modulos mo ON mo.ciclos = m.ciclos AND mo.curso = m.curso ");  // Cambiado: mo.ciclos en lugar de mo.cicloId
            hql.append("JOIN Horarios h ON h.modulos = mo ");
            hql.append("WHERE h.users.id = :profesorId ");
            hql.append("AND u.tipos.id = 4 "); // alumnos (tipo_id = 4)

            if (cicloId != null) {
                hql.append(" AND m.ciclos.id = :cicloId ");
            }

            if (curso != null) {
                hql.append(" AND m.curso = :curso ");
            }

            hql.append("ORDER BY u.apellidos, u.nombre");

            System.out.println("[DAO] HQL: " + hql.toString());

            Query<Users> query = session.createQuery(hql.toString(), Users.class);
            query.setParameter("profesorId", profesorId);

            if (cicloId != null) {
                query.setParameter("cicloId", cicloId);
            }

            if (curso != null) {
                query.setParameter("curso", curso);
            }

            alumnos = query.getResultList();
            
            System.out.println("[DAO] Alumnos encontrados: " + alumnos.size());
            
        } catch (Exception e) {
            System.err.println("[DAO ERROR] " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return alumnos;
    }
    
    /**
     * Obtiene profesores con filtros opcionales de ciclo y curso
     * @param cicloId ID del ciclo (null para todos)
     * @param curso Número de curso (null para todos)
     * @return Lista de profesores que cumplen los criterios
     */
    public List<Users> getProfesoresByFilters(Integer cicloId, Integer curso) {
        
        List<Users> profesores;

        System.out.println("[DAO] Buscando profesores - Ciclo: " + cicloId + ", Curso: " + curso);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            StringBuilder hql = new StringBuilder();
            hql.append("SELECT DISTINCT p ");
            hql.append("FROM Users p ");
            hql.append("WHERE p.tipos.id = 3 ");  
            
            if (cicloId != null || curso != null) {
                hql.append("AND EXISTS (");
                hql.append("    SELECT 1 ");
                hql.append("    FROM Horarios h ");
                hql.append("    JOIN h.modulos mo ");
                hql.append("    WHERE h.users.id = p.id ");
                
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

            profesores = query.getResultList();
            
            System.out.println("[DAO] Profesores encontrados: " + profesores.size());
            
        } catch (Exception e) {
            System.err.println("[DAO ERROR] " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return profesores;
    }
}