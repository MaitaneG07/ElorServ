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

	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

	        StringBuilder hql = new StringBuilder();
	        hql.append("SELECT DISTINCT u ");
	        hql.append("FROM Users u ");
	        hql.append("JOIN Matriculaciones m ON m.users = u ");
	        hql.append("JOIN Modulos mo ON mo.ciclos = m.ciclos AND mo.curso = m.curso ");
	        hql.append("JOIN Horarios h ON h.modulos = mo ");
	        hql.append("WHERE h.users.id = :profesorId ");
	        hql.append("AND u.tipoId = 4 "); // alumnos

	        if (cicloId != null) {
	            hql.append("AND m.ciclos.id = :cicloId ");
	        }

	        if (curso != null) {
	            hql.append("AND m.curso = :curso ");
	        }

	        hql.append("ORDER BY u.apellidos, u.nombre");

	        Query<Users> query = session.createQuery(hql.toString(), Users.class);
	        query.setParameter("profesorId", profesorId);

	        if (cicloId != null) {
	            query.setParameter("cicloId", cicloId);
	        }

	        if (curso != null) {
	            query.setParameter("curso", curso);
	        }

	        alumnos = query.getResultList();
	    }

	    return alumnos;
	}
}
