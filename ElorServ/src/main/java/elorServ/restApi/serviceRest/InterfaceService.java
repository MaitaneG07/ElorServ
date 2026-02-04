package elorServ.restApi.serviceRest;

import java.util.List;

public interface InterfaceService<T> {
	
	 T save(T t);

	    List<T> findAll();

	    T actualizar(Long id, T t);

	    void deleteById(Long id);

}
