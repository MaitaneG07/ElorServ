package elorServ.modelo.dao;

import java.util.List;

public interface DaoInterface <I>{

	public List <I> save ();
	
	public I findById (int id);
	
	public void findAll (I i);
	
	public void update (I i);
	
	public void delete(I i);
	
	
}
