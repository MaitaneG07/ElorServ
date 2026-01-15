package elorServ.modelo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import elorServ.modelo.util.HibernateUtil;



public abstract  class GenericDao<T> implements DaoInterface<T> {
	
	private Class<T> entityClass = null;
	
	 protected GenericDao(Class<T> entityClass) {
	        this.entityClass = entityClass;
	    }

	public List<T> selectAll() {
		List<T> ret = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            ret = session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error en save(): " + e.getMessage());
        }
        return ret;
    }

	public T selectById(int id) {
		 T ret = null;
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            Transaction tx = session.beginTransaction();
	            ret = session.get(entityClass, id);
	            tx.commit();
	        } catch (Exception e) {
	            System.out.println("Error en find(): " + e.getMessage());
	        }
	        return ret;
	    }

	public void insert(T i) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(i);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error en findAll(): " + e.getMessage());
        }
    }

	public void update(T i) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(i);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error en update(): " + e.getMessage());
        }
    }
	
	public void delete(T i) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            T managed = (T) session.merge(i);
            session.remove(managed);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error en delete(): " + e.getMessage());
        }
    }

}


