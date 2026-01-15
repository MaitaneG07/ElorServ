package elorServ.modelo.dao;

import java.util.List;

public interface DaoInterface <I>{

	public List <I> selectAll ();
	
	public I selectById (int id);
	
	public void insert (I i);
	
	public void update (I i);
	
	public void delete(I i);
	
	
	
}
