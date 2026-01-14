package modelo.dao;

import java.util.List;

public interface DaoInterface <T, t>{

	  void save(T entity);
	    T getById(t id);
	    List<T> getAll();
	    void update(T entity);
	    void delete(T entity);
	    void deleteById(t id);
}
